<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

	<%@include file="../includes/header.jsp"  %>
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header"> Board Modify/Delete</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Board Modify/Delete
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <form role="form" action="/board/modify" method="post">
                            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                          	<input type="hidden" name="pageNum" value="${cri.pageNum }">
                          	<input type="hidden" name="amount" value="${cri.amount }">
							<input type="hidden" name="type" value="${cri.type }">
                            <input type="hidden" name="keyword" value="${cri.keyword }">
                      		<div class="form-group">
                                  <label>BNO</label>
                                  <input class="form-control" name="bno" readonly="readonly" value="<c:out value='${board.bno }' />">
                              </div>
                              
                      		<div class="form-group">
                                  <label>Title</label>
                                  <input class="form-control" name="title" value="<c:out value='${board.title }' />">
                              </div>
                              
                              <div class="form-group">
                                  <label>Content</label>
                                  <textarea class="form-control" rows="5" cols="50" name="content"><c:out value='${board.content }' /></textarea>
                              </div>
                              
                              <div class="form-group">
                                  <label>Writer</label>
                                  <input class="form-control" name="writer" readonly="readonly" value="<c:out value='${board.writer}'/>">
                              </div>
                              <sec:authentication property="principal" var="pinfo"/>
                              <sec:authorize access="isAuthenticated()">
                              	<c:if test="${pinfo.username eq board.writer }">
	                              <button class="btn btn-default" data-oper="modify">Modify</button>
	                              <button class="btn btn-danger" data-oper="remove">Remove</button>
	                            </c:if>
                              </sec:authorize>
                              <button class="btn btn-info" data-oper="list">List</button>
                           
                            
                            <!-- /.table-responsive -->
                            </form>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- 첨부파일 영역 -->
			 <div class="bigPictureWrapper">
			 	<!-- 이미지 보여줄 영역 -->
			 	<div class="bigPicture">
			 	</div>
			 </div>
			 <div class="row">
			 	<div class="col-lg-12">
			 		<div class="panel panel-default">
			 			<div class="panel-heading">Files</div>
			 			<div class="panel-body">
			 				
			 				<!-- 파일선택 -->	
			 				<div class="form-group uploadDiv">
			 					<input type="file" name="uploadFile" multiple>
			 				</div>
			 				
			 				<!-- 첨부파일 List조회 -->
			 				<div class="uploadResult">
			 					<ul>
			 					</ul>
			 				</div>
			 			</div>
			 		</div>
			 	</div>
			 </div>
<script>
$(document).ready(function(){
	
	var formObj = $("form");
	$(".btn").click(function(e){
		e.preventDefault();
		var operation = $(this).data("oper");
		
		console.log(operation);
		
		if(operation == 'list'){
// 			self.location = '/board/list';
			formObj.attr("action", "/board/list")
			   .attr("method","get");
			
		}else if(operation == 'remove'){
			formObj.attr("action", "/board/remove")
				   .attr("method","post");
				   
		}else if(operation == 'modify'){
			console.log("modify..........");
			
			var str = "";
			
			$(".uploadResult ul li").each(function(i, obj){
				var jobj = $(obj);	//첨부파일 목록
				
				console.log(jobj);
				
				str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+jobj.data("filetype")+"'>";
				
			});
			console.log("========str=======");
			console.log(str);
			
			//formObj.attr("action", "/board/modify").attr("method","post");
			formObj.append(str).submit();
		}
		
		formObj.submit();
		
	});	//end of btnClick
	
	(function(){
		var bno = '<c:out value="${board.bno}" />';
		
		$.getJSON('/board/getAttachList',{bno : bno}, function(arr){
			console.log(arr);
			
			var str = "";
			
			$(arr).each(function(i, attach){
				
				//image인 경우
				if(attach.fileType){
					var fileCallPath = encodeURIComponent( attach.uploadPath+"\\" + "s_" + attach.uuid + "_" + attach.fileName );	//파일경로
					
					str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename=\'"+attach.fileName+"\' data-filetype='"+attach.fileType+"'>";
					str += "<div>";
					str += "<span>"+attach.fileName+"</span> ";
					str += "<button type='button' class='btn-warning btn-circle' data-file='"+fileCallPath+"'><i class='fa fa-times'></i></button><br>";
					str += "<img src='/display?fileName="+fileCallPath+"'";
					str += "</div>";
					str += "</li>";
					
				}else{	//file인 경우
					str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-filetype='"+attach.fileType+"'>";
					str += "<div>";
					str += "<span>"+attach.fileName+"</span> ";
					str += "<button type='button' class='btn-warning btn-circle' data-file=\'"+fileCallPath+"\'><i class='fa fa-times'></i></button><br>";
					str += "<img src='/resources/img/attach.png'>";
					str += "</div>";
					str += "</li>";
				}
			});
			
			$(".uploadResult ul").html(str);
			
		});	//end of $.getJSON
	})();	//end of function
		
	//첨부파일 목록 클릭시 파일일 경우 다운로드, 이미지일경우 원본이미지 보여주기
	$(".uploadResult").on("click","li",function(e){
		var liObj = $(this);	//<li>요소
		
		var path = encodeURIComponent( liObj.data("path") + "\\" + liObj.data("uuid") + "_" + liObj.data("filename") );	//파일경로
		
		//파일타입이 이미지인 경우
		if(liObj.data("filetype")){
			showImage(path.replace(new RegExp(/\\/g),"/"));
			
		}else{	//파일타입이 파일인 경우
			self.location="/download?fileName="+ path;
		}
	});	
	
	//원본이미지 혹은 주변 배경을 선택하면
	$(".bigPictureWrapper").on("click", function(e){
		
		$(".bigPicture").animate({width:'0%', height:'0%'},1000); //이미지를 화면 중앙으로 작게 점차 줄여줍니다.(1초 동안)
		
		//원본 이미지창 닫기
		setTimeout(function(){
			$(".bigPictureWrapper").hide();
		}, 1000);
	});
	
	//x 버튼 클릭시 
	$(".uploadResult").on("click", "button",function(e){
		console.log("delete file");
		
		if(confirm("delete this file ?")){
			var targetLi = $(this).closest("li");
			targetLi.remove();
		}
	});
	
	  var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
	  var maxSize = 5242880; //5MB
	  
	  function checkExtension(fileName, fileSize){
	    
	    if(fileSize >= maxSize){
	      alert("파일 사이즈 초과");
	      return false;
	    }
	    
	    if(regex.test(fileName)){
	      alert("해당 종류의 파일은 업로드할 수 없습니다.");
	      return false;
	    }
	    return true;
	  }
	  
	  var csrfHeaderName ="${_csrf.headerName}"; 
	  var csrfTokenValue="${_csrf.token}";

	  
	  $("input[type='file']").change(function(e){

	    var formData = new FormData();
	    
	    var inputFile = $("input[name='uploadFile']");
	    
	    var files = inputFile[0].files;
	       
	    for(var i = 0; i < files.length; i++){

	      if(!checkExtension(files[i].name, files[i].size) ){
	        return false;
	      }
	      formData.append("uploadFile", files[i]);
	      
	    }
	    
	    $.ajax({
	      url: '/uploadAjaxAction',
	      processData: false, 
	      contentType: false,
	      data: formData,type: 'POST',
	      beforeSend: function(xhr) {
	          xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
	      },
	      dataType:'json',
	        success: function(result){ 
			  showUploadResult(result); //업로드 결과 처리 함수 

	      }
	    }); //$.ajax
	    
	    function showUploadResult(uploadResultArr){
		    
	        if(!uploadResultArr || uploadResultArr.length == 0){ return; }
	        
	        var uploadUL = $(".uploadResult ul");
	        
	        var str ="";
	        
	        $(uploadResultArr).each(function(i, obj){
	    		
	    		if(obj.fileType){
	    			var fileCallPath =  encodeURIComponent( obj.uploadPath+ "/s_"+obj.uuid +"_"+obj.fileName);
	    			
	    			str += "<li data-path='"+obj.uploadPath+"'";
	    			str +=" data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-filetype='"+obj.fileType+"'"
	    			str +" ><div>";
	    			str += "<span> "+ obj.fileName+"</span>";
	    			str += "<button type='button' data-file=\'"+fileCallPath+"\' "
	    			str += "data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
	    			str += "<img src='/display?fileName="+fileCallPath+"'>";
	    			str += "</div>";
	    			str +"</li>";
	    		}else{
	    			var fileCallPath =  encodeURIComponent( obj.uploadPath+"/"+ obj.uuid +"_"+obj.fileName);			      
	    		    var fileLink = fileCallPath.replace(new RegExp(/\\/g),"/");
	    		      
	    			str += "<li "
	    			str += "data-path='"+obj.uploadPath+"' data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-filetype='"+obj.fileType+"' ><div>";
	    			str += "<span> "+ obj.fileName+"</span>";
	    			str += "<button type='button' data-file=\'"+fileCallPath+"\' data-type='file' " 
	    			str += "class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
	    			str += "<img src='/resources/img/attach.png'></a>";
	    			str += "</div>";
	    			str +"</li>";
	    		}

	        });
	        
	        uploadUL.append(str);
	    }
	    
	  });   
	
});

//원보이미지 보여주기
function showImage(fileCallPath){
	//alert(fileCallPath);
	
	/* display : flex; 속성은 이미지를 아래가 아닌 옆으로 조회해준다.  */
	$(".bigPictureWrapper").css("display","flex").show();
	
	/* animate() 속성  : 부드럽게 이미지를 보여줌(1초) */
	$(".bigPicture").html("<img src='/display?fileName="+fileCallPath+"'>").animate({width:'100%', height: '100%'}, 1000);
}
</script>
            <!-- /.row -->
	<%@include file="../includes/footer.jsp" %>