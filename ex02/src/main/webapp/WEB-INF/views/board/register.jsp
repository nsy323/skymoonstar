<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

	<%@include file="../includes/header.jsp"  %>
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header"> Board Register</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Board Register
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            	<form role="form" action="/board/register" method="post">
                            		<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                            		<div class="form-group">
                                        <label>Title</label>
                                        <input class="form-control" name="title">
                                    </div>
                                    
                                    <div class="form-group">
                                        <label>Content</label>
                                        <textarea class="form-control" rows="5" cols="50" name="content"></textarea>
                                    </div>
                                    
                                    <div class="form-group">
                                        <label>Writer</label>
                                        <input class="form-control" name="writer" 
                                        	value='<sec:authentication property="principal.username"/>' readonly="readonly">
                                    </div>
                                    <button type="submit" class="btn btn-default">Submit Button</button>
                                    <button type="reset" class="btn btn-default">Reset Button</button>
                            	</form>
                            
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            
            <!-- 첨부파일 시작 -->
            <div class="row">
            	<div class="col-lg-12">
            		<div class="panel panel-default">
            			<div class="panel-heading">File Attach</div>
            			<div class="panel-body">
            				<div class="form-group uploadDiv">
            					<input type="file" name="uploadFile" multiple>
            				</div>
            				<div class="uploadResult">
            					<ul>
            					</ul>
            				</div>
            			</div>
            		</div>
            	</div>
            </div>
            <!-- 첨부파일 끝 -->
            
<script>
	$(document).ready(function(e){
	
		var formObj = $("form[role='form']");
		
		//Submit Button 클릭시
		$("button[type='submit']").on("click", function(e){
			e.preventDefault();
			
			console.log("submit clicked");
			
			var str = "";
			
			//submit Button 클릭시 
			$(".uploadResult ul li").each(function(i, obj){
				var jobj = $(obj);
				
				console.dir(jobj);		//요소를 JSON과 같은 형태로 출력
				
				str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+ jobj.data("filename") +"'>";
				str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+ jobj.data("uuid") +"'>";
				str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+ jobj.data("path") +"'>";
				str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+ jobj.data("type") +"'>";
				
			}); //end of $(".uploadResult ul li").each(function(i, obj)
		
			formObj.append(str).submit();
			
		});
		
		
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$"); //파일 확장자 체크 정규표현식
		var maxSize = 5242880; //50MB (1024*1024*50)
		
		//파일 확장자,크기 체크
		function checkExtension(fileName, fileSize){
			//파일 크키 확인
			if(fileSize >= maxSize){
				alert("파일 사이즈 초과");
				return false;
			}
			
			//파일 확장자 확인
			if(regex.test(fileName)){
				alert("해당 종류의 파일은 업로드할 수 없습니다.");
				return false;
			}
			
			return true;
		}
		
		/* 
			
			csrf토근의 값은 세션이 달라질 때마다 변함
			ajax로 데이터 전송시 beforeSend를 이용해서 추가적인 헤더를 지정해서 전송해야 함,안그럼 403에러남
			
		*/
		var csrfHeaderName = "${_csrf.headerName}";	//X-CSRF-TOKEN
		var csrfTokenValue = "${_csrf.token}";		//f1378ba8-4414-4bd7-4be1a-b7d0a4f664942sub0
		
		
		//파일 선택시
		$("input[type='file']").change(function(e){
			
			var formData = new FormData();
			
			var inputFile = $("input[name='uploadFile']");
			
			var files = inputFile[0].files;
			
			for(var i = 0; i < files.length; i++){
			
				if(!checkExtension(files[i].name, files[i].size)){
					return false;
				}
				
				formData.append("uploadFile", files[i]);
			}
			
			console.log(formData);
			
			$.ajax({
			      url: '/uploadAjaxAction',
			      processData: false, 
			      contentType: false,
			      beforeSend: function(xhr){		// csrf 헤더정보 전송
			    	xhr.setRequestHeader(csrfHeaderName, csrfTokenValue)  
			      },
			      data: formData,
			      type: 'POST',
			      dataType:'json',
			        success: function(result){
			          console.log(result); 
					
			          showUploadResult(result); //업로드 결과 처리 함수 

			      }
			    }); //$.ajax
		});
			
		//첨부한 파일 list 보여주기
		function showUploadResult(uploadResultArr){
			
			//파일 첨부하지 않았을 경우
			if(!uploadResultArr || uploadResultArr.length ==  0){ return; }
			
			var uploadUL = $(".uploadResult ul"); //파일 upload List
			
			var str = "";
			
			$(uploadResultArr).each(function(i, obj){
				
				if(obj.fileType){	//이미지인 경우
					
					/*
					*	encodeURIComponent() : uri호출에 적합한 문자열로 인코딩 처리 (한글, 공백문자)
					* 	섬네일 파일 : 파일경로/s_uuid_파일이름
					*/
				
					console.log("파일경로: " + obj.uploadPath + "\\s_" + obj.uuid + "_" + obj.fileName);
					
					var fileCallPath = encodeURIComponent(obj.uploadPath + "\\s_" + obj.uuid + "_" + obj.fileName);  //섬네일 파일경로
					
					var originPath = obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName;	//실제파일경로
					
					originPath = originPath.replace(new RegExp(/\\/g),"/");
					
					str += "<li data-path='"+ obj.uploadPath +"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.fileType+"'><div>";
					str	+= "<span>" + obj.fileName + "</span> ";
					str	+= "<button type='button' class='btn btn-warning btn-circle' data-file=\'"+ fileCallPath +"\' data-type='image'>";
					str	+= "<i class='fa fa-times'></i></button><br>";
					str	+= "<img src='/display?fileName="+ fileCallPath +"'>";
					str	+= "</div></li>";
					
				}else{	//이미지가 아닌경우
					
					var fileCallPath = encodeURIComponent(obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName);  //파일경로
					var fileLink = fileCallPath.replace(new RegExp(/\\/g), "/");
					
					console.log("fileCallPath: "+fileCallPath);
					
					str += "<li data-path='"+ obj.uploadPath +"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.fileType+"'><div>";
					str	+= "<span>" + obj.fileName + "</span> ";
					str	+= "<button type='button' class='btn btn-warning btn-circle' data-file=\'"+ fileCallPath +"\' data-type='file'>";
					str	+= "<i class='fa fa-times'></i></button><br>";
					str	+= "<img src='/resources/img/attach.png'>";
					str	+= "</div></li>";
				}
				
			});
				
			uploadUL.append(str);
		}
		
		//버튼클릭시 파일 삭제 처리
		$(".uploadResult").on("click","button", function(e){
			
			var csrfHeaderName = "${_csrf.headerName}";	//X-CSRF-TOKEN
			var csrfTokenValue = "${_csrf.token}";		//f1378ba8-4414-4bd7-4be1a-b7d0a4f664942sub0
			
			console.log("delete file");
			
			var targetFile = $(this).data("file");	//파일경로
			var type = $(this).data("type");		//파일타입
			
			var targetLi = $(this).closest("li"); 	//button에서 가장 가까운 li 
			
			console.log(targetFile);
			
			$.ajax({
				url : "/deleteFile",
				data: {fileName : targetFile, type : type},
				dataType: "text",
				type: "post",
				beforeSend: function(xhr){		// csrf 헤더정보 전송
			    	xhr.setRequestHeader(csrfHeaderName, csrfTokenValue)  
			      },
				success : function(result){
					alert(result);
					
					targetLi.remove();	//보여지는 파일도 삭제
				}
				
			}); //end of ajax
		})

	
	});
</script>
            
	<%@include file="../includes/footer.jsp" %>