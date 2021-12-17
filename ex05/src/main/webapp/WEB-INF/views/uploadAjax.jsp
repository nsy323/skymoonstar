<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>upload with Ajax</title>
<script src="https://code.jquery.com/jquery-3.3.1.min.js"
	integrity="sha256-FgpCb/KJQlLNfOu91ta32o/NMZxltwRo8QtmkMRdAu8="
	crossorigin="anonymous"></script>
<style>
 .uploadResult {
 	width : 100%;
 	background-color : grey;
 }
 
 .uploadResult ul{
 	display: flex;
 	flex-flow: row;
 	justify-content : center;
 	align-items: center;
 }
 
 .uploadResult ul li{
 	list-style: none;
 	padding: 10px;
 }
 
 .uploadReuslt ul li img{
 	width:20px;
 }
 
 .bigPictureWrapper{
 	position:absolute;
 	display: none;
 	justify-content: center;
 	align-items: center;
 	top : 0%;
 	width: 100%;
 	height: 100%;
 	background-color: grey;
 	z-index: 100;
 	background: rgba(255,255,255,0.5);
 }
 
 .bigPicture{
 	position:relative;
 	display: flex;
 	justify-content: center;
 	align-items: center;
 }
 
 .bigPicture img{
 	width: 600px;
 }
 
</style>
<script>
$(document).ready(function(){
	
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
	
	//clone() : 선택한 요소 복제
	var cloneObj = $(".uploadDiv").clone();
	
	//upload버튼 클릭시
	$("#uploadBtn").on("click", function(e){
		var formData = new FormData();
		
		var inputFile = $("input[name='uploadFile']");
		
		var files = inputFile[0].files;
		
		console.log(files);
		
		//add filedata to formdata
		for(var i = 0 ; i < files.length; i++){
		
			//파일확장자, 크기 체크
			if(!checkExtension(files[i].name, files[i].size)){
				return false;
			}
			
			formData.append("uploadFile",files[i]);	//파일정보 append
			
			console.log("[" +i+ "]");
			console.log(files[i]);
		}
		
		console.log("---------------");
	
		//파일 저장
		$.ajax({
			url : "/uploadAjaxAction",
			processData : false, 
			contentType  : false,
			data : formData,
			type : "post",
			dataType:'json',
			success : function(result){	//upload 성공시
				console.log(result);
				
				//alert("Uploaded");
				
				showUploadedFile(result);		//uploadFileResult영역에 파일보여주기
				
				$(".uploadDiv").html(cloneObj.html());	//파일 최기화
				
				
				
			}
			
		});//end ajax
		
	});//end click
	
	var uploadResult = $(".uploadResult ul"); //파일 upload List
	
	//첨부한 파일 list 보여주기
	function showUploadedFile(uploadResultArr){
		var str = "";
		
		$(uploadResultArr).each(function(i, obj){
			
			//이미지가 아닌경우
			if(!obj.image){
				var fileCallPath = encodeURIComponent(obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName);  //파일경로
				console.log("fileCallPath: "+fileCallPath);
				str += "<li><div><a href='/download?fileName="+ fileCallPath +"'>"
					+  "<img src='/resources/img/attach.png'>" + obj.fileName + "</a>"
					+  "<span data-file=\'"+ fileCallPath +"\' data-type='file'> x </span>"
 					+  "</div></li>";
					
			}else{//이미지인 경우
				
				//str += "<li>" + obj.fileName + "</li>";
				
			/*
			*	encodeURIComponent() : uri호출에 적합한 문자열로 인코딩 처리 (한글, 공백문자)
			* 	섬네일 파일 : 파일경로/s_uuid_파일이름
			*/
			
				console.log("파일경로: " + obj.uploadPath + "\\s_" + obj.uuid + "_" + obj.fileName);
				var fileCallPath = encodeURIComponent(obj.uploadPath + "\\s_" + obj.uuid + "_" + obj.fileName);  //섬네일 파일경로
				
				var originPath = obj.uploadPath + "\\" + obj.uuid + "_" + obj.fileName;	//실제파일경로
				
				originPath = originPath.replace(new RegExp(/\\/g),"/");
				
				str += "<li><a href=\"javascript:showImage(\'"+originPath+"\')\">"
					+  "<img src='/display?fileName="+fileCallPath+"'></a>"
					+  "<span data-file=\'"+ fileCallPath +"\' data-type='image'> x </span>"		
					+  "</li>";
			}
			
		});
		
		uploadResult.append(str);
	}
	
	//원본이미지 혹은 주변 배경을 선택하면
	$(".bigPictureWrapper").on("click", function(e){
		
		$(".bigPicture").animate({width:'0%', height:'0%'},1000); //이미지를 화면 중앙으로 작게 점차 줄여줍니다.(1초 동안)
		
		//1초 후에 배경창이 안보이도록 처리
		//(IE인 경우 $('.bicPictureWrapper').hide(); 로 변경 후 사용)
		setTimeout(() => {
			$(this).hide();	
		}, 1000);
		
		//IE인경우
// 		setTimeout(function(){
// 			$(".bigPictureWrapper").hide();
// 		}, 1000);
	});
	
	// x (삭제) 를 클릭하면
	$(".uploadResult").on("click","span",function(e){
		var targetFile = $(this).data("file");	//파일경로
		var type = $(this).data("type");		//파일타입
		
		console.log(targetFile);
		
		$.ajax({
			url : "/deleteFile",
			data: {fileName : targetFile, type : type},
			dataType: "text",
			type: "post",
			success : function(result){
				
				alert(result);
			}
			
		}); //end of ajax
		
	});
});

//원본이미지를 보여줄 <div>처리
function showImage(fileCallPath){
	//alert(fileCallPath);
	/* display : flex; 속성은 이미지를 아래가 아닌 옆으로 조회해준다.  */
	$(".bigPictureWrapper").css("display","flex").show();
	
	/* animate() 속성  : 부드럽게 이미지를 보여줌(1초) */
	$(".bigPicture").html("<img src='/display?fileName="+fileCallPath+"'>").animate({width:'100%', height: '100%'}, 1000);
}

</script>

</head>
<body>
<form id="uploadForm" method="post" enctype="multipart/form-data">
<div class="uploadDiv">
	<input type="file" name="uploadFile" multiple>
</div>
</form>

<button id="uploadBtn" type="button">Upload</button>

<!-- 첨부한 문서 List 보여주기 -->
<div class="uploadResult">
	<ul>
	
	</ul>
</div>

<!-- 섬네일이미지 클릭시 원본사진이미지 보여주기 영역 -->
<div class="bigPictureWrapper">
	<div class="bigPicture"> 
	</div>
</div>

</body>
</html>