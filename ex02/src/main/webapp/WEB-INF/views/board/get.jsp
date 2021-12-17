<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>

	<%@include file="../includes/header.jsp"  %>

            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header"> Board Read</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Board Read
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            		
                      		<div class="form-group">
                                  <label>BNO</label>
                                  <input class="form-control" name="title" readonly="readonly" value="<c:out value='${board.bno }' />">
                              </div>
                              
                      		<div class="form-group">
                                  <label>Title</label>
                                  <input class="form-control" name="title" readonly="readonly" value="<c:out value='${board.title }' />">
                              </div>
                              
                              <div class="form-group">
                                  <label>Content</label>
                                  <textarea class="form-control" rows="5" cols="50" name="content" readonly="readonly"><c:out value='${board.content }' /></textarea>
                              </div>
                              
                              <div class="form-group">
                                  <label>Writer</label>
                                  <input class="form-control" name="writer" readonly="readonly" value="<c:out value='${board.writer}'/>">
                              </div>
                              <button type="button" class="btn btn-default listBtn"><a href="/board/list">List</a></button>
                              
                              <sec:authentication property="principal" var="pinfo" />
                              
                              <sec:authorize access="isAuthenticated()">
                              	<c:if test="${pinfo.username eq board.writer}">
                              		<button type="button" class="btn btn-default modBtn">
                              			<a href="/board/modify?bno=<c:out value='${board.bno }' />">modify</a>
                              		</button>
                              	</c:if>
                           	  </sec:authorize>
                           	  
	                          <form id="actionForm" action="/board/list" method="get">
	                          	<input type="hidden" name="pageNum" value="${cri.pageNum }">
	                          	<input type="hidden" name="amount" value="${cri.amount }">
	                          	<input type="hidden" name="bno" value="${board.bno}">
	                          	<input type="hidden" name="type" value="${cri.type }">
                            	<input type="hidden" name="keyword" value="${cri.keyword }">
	                          </form>


<script type="text/javascript" src="/resources/js/reply.js"></script>
<script>

/* 
console.log("===========");
console.log("JS TEST");

var bnoValue = '<c:out value="${board.bno}"/>';


//for replyService add test 댓글 등록
replyService.add(
		{reply:"JS Test", replyer: "tester", bno : bnoValue},
		function (result){
			alert("result : " + result);	
		}
);

//댓글 목록 조회
replyService.getList(
		{bno:bnoValue, page: 1},
		function(list){
			for(var i = 0,  len  = list.length||0; i < len; i++){
				console.log(list[i]);	
			}
		}
);

//댓글 삭제
replyService.remove(
		46,
		function(count){
			console.log('count : ' + count);
			
			if(count == "success"){
				alert("REMOVED");
			}
		}, function(err){
			alert("ERROR.." + err);
		}
);

//댓글수정
replyService.modify(
		{ 
			rno : 17,
			bno : bnoValue,
			reply : "Modified Reply....."
		},
		function(result){
			alert("수정 완료...");
		}
)

//댓글조회
replyService.get(
		8,
		function(data){
			console.log(data);
		}
	
)

*/

</script>
	                          
<script>
$(document).ready(function(){
	
	var actionForm = $("#actionForm");
	
	//목록조회
	$('.listBtn').click(function(e){
		e.preventDefault();
		actionForm.find("input[name='bno']").remove();
		actionForm.submit();
		
	});
	
	//수정
	$('.modBtn').click(function(e){
		e.preventDefault();
		actionForm.attr("action","/board/modify");
		actionForm.submit();
		
	});
	
	var bnoValue = '<c:out value="${board.bno}" />';
	var replyUL = $(".chat");
	
	showList(1);
	
	//댓글목록 조회
	function showList(page){
		
		console.log("page : " + page);
		replyService.getList(
				{
					bno : bnoValue,
					page : page || 1
				},
				function(replyCnt, list){
					var str = "";
					
					console.log("replyCnt : " + replyCnt);
					console.log(list);					
					
					//등록시 마지막페이지 조회
					if(page == -1){
						pageNum = Math.ceil(replyCnt/10.0);
						
						showList(pageNum);
						
						return;
					}
					
					//댓글내역이 없을 경우
					if(list == null || list.length == 0){
						replyUL.html("");
						return;
					}
					
					//댓글목록 추가
					for(var i = 0,  len = list.length||0; i < len; i++){	
						str += "<li class='left clearfix' data-rno='"+ list[i].rno +"'>";
						str += "<div>";
						str += "<div class='header'><strong class='primary-font'>"+ list[i].replyer +"</strong>";
						str += "<small class='pull-right text-muted'>" + replyService.displayTime(list[i].replyDate) + "</small>";
						str += "<p>"+ list[i].reply +"</p>";
						str += "</div>";
						str += "</div>";
						str += "</li>";
					}
					replyUL.html(str);
					
					showReplyPage(replyCnt);
					
				}	//end function
		);	//end getList
	}	//end showList
	
	var modal = $(".modal");									//모달창
	var modalInputReply = modal.find("input[name='reply']");	//reply(댓글) input
	var modalInputReplyer = modal.find("input[name='replyer']");	//replyer(댓글 입력자) input
	var modalInputReplyDate = modal.find("input[name='replyDate']"); //replyDate(댓글 입력일자) input
	
	var modalModBtn = $("#modalModBtn");		//수정버튼
	var modalRemoveBtn = $("#modalRemoveBtn");	//삭제버튼
	var modalRegisterBtn = $("#modalRegisterBtn");	//등록버튼
	
	var replyer = null;	//사용자아이디
	
	//스크링 시큐리티를 이용하여 사용자명 replayer 변수에 담기
	<sec:authorize access="isAuthenticated()">
		replyer = '<sec:authentication property="principal.username" />';
	</sec:authorize>
	
	//댓글등록 모달창 show
	$("#addReplyBtn").on("click", function(){
		modal.find("input").val("");	//input 값 초기화
		modal.find("input[name='replyer']").val(replyer);		//댓글등록자
		modalInputReplyDate.closest("div").hide(); //replyDate에 가장 가까운 div 숨기기
		modal.find("button[id != 'modalCloseBtn']").hide(); //닫기버튼이 아닌 것만 숨기기
		
		modalRegisterBtn.show();	//등록버튼 보여주기
		
		$(".modal").modal("show");	//모달창 show
	});
	
	/* 
	
	csrf토근의 값은 세션이 달라질 때마다 변함
	ajax로 데이터 전송시 beforeSend를 이용해서 추가적인 헤더를 지정해서 전송해야 함,안그럼 403에러남
	
	*/
	var csrfHeaderName = "${_csrf.headerName}";	//X-CSRF-TOKEN
	var csrfTokenValue = "${_csrf.token}";		//f1378ba8-4414-4bd7-4be1a-b7d0a4f664942sub0
	
	//Ajax spring security header...(모든 Ajax 전송시 csrf토큰 같이 전송하도록 세팅, 매번 beforeSend하지않아도 됨)
	$(document).ajaxSend(function(e, xhr, options){
		xhr.setRequestHeader(csrfHeaderName, csrfTokenValue);
	});
	
	//댓글등록 버튼 클릭
	$("#modalRegisterBtn").on("click", function(){
		var reply = {
				reply : modalInputReply.val(), 
				replyer : modalInputReplyer.val(),
				bno : bnoValue
		}
		
		//ajax로 insert 호출
		replyService.add(reply, function(result){
				alert(result);
				
				modal.find("input").val();	//input 값 초기화
				modal.modal("hide");		//모달창 숨기기
				
				//showList(1);	//댓글목록 갱신
				showList(-1);	//댓글목록 갱신				
			}
		)
	});
	
	//댓글수정 버튼 클릭
	$("#modalModBtn").on("click",function(e){
		
		var originalReplyer =  modalInputReplyer.val();
			
		var reply = {
				rno : modal.data("rno"),
				reply : modalInputReply.val(),
				replyer : originalReplyer
		}
		
		if(!replyer){
			alert("로그인 후 수정이 가능합니다.");
			modal.modal("hide");
			return;
		}
		
		console.log("original Replyer : " + originalReplyer);
		
		if(replyer != originalReplyer){
			alert("자신이 작성한 댓글만 수정이 가능합니다.");
			modal.modal("hide");
			return;
		}
		
		replyService.modify(reply, function(result){
				alert(result);
				modal.modal("hide");
				showList(pageNum);
			}
		)
	});
	
	//댓글삭제버튼 클릭
	$("#modalRemoveBtn").on("click", function(result){
		var rno = modal.data("rno");
		
		console.log("rno : " + rno);
		console.log("replyer :" + replyer);
		
		if(!replyer){
			alert("로그인 후 삭제가 가능합니다.");
			modal.modal("hide");
			return;
		}
		
		var originalReplyer = modalInputReplyer.val();
		
		console.log("Original Replyer : " + modalInputReplyer.val());	//댓글의 원래 작성자
		
		if(replyer != originalReplyer){
			alert("자신이 작성한 댓글만 삭제 가능합니다.");
			modal.modal("hide");
			return;
		}
		
		replyService.remove(rno,originalReplyer, function(result){
			alert(result);
			modal.modal("hide");
			showList(pageNum);
		})
	});
	
	//댓글 목록 클릭
	$(".chat").on("click","li",function(){
			var rno = $(this).data("rno");	//댓글 일련번호

			console.log("rno :" + rno);
			
			//댓글 조회
			replyService.get(rno ,function(reply){
				modalInputReply.val(reply.reply);
				modalInputReplyer.val(reply.replyer);
				modalInputReplyDate.val(replyService.displayTime(reply.replyDate)).attr("readonly","readonly");
				modal.data("rno",reply.rno);
				
				modal.find("button[id!='modalCloseBtn']").hide();
				modalModBtn.show();		//수정버튼
				modalRemoveBtn.show();	//삭제버튼
				
				$(".modal").modal("show");
				
			});
	});
	
	/* 댓글 페이징 처리 */
	var pageNum = 1;	//현재페이지
	var replyPageFooter = $(".panel-footer");	//페이징위치
	 
	//페이징 처리
	function showReplyPage(replyCnt){
		var endNum = Math.ceil(pageNum/10.0)*10;
		var startNum = endNum -9;	//시작페이지
		console.log("endNum : " + endNum + ", startNum : " + startNum);
		var prev = startNum != 1;	//이전
		var next = false;			//다음
		
		if(endNum * 10 >= replyCnt){
			endNum = Math.ceil(replyCnt/10.0);
		}
		
		if(endNum * 10 < replyCnt){
			next = true;
		}
		
		var str = '<div><ul class="pagination pull-right">';
		
		if(prev){
			console.log("aaa");
			str += '<li class="page-item"><a class="page-link" herf="' + (startNum -1) + '">Previous</a></li>';
		}
		
		for(var i = startNum; i <= endNum; i++){
			var active = (pageNum == i)? 'active': '';
			
			str += '<li class="page-item '+ active +'"><a class="page-link" href="'+i+'">'+ i +'</a></li>';
		}
		
		if(next){
			str += '<li class="page-item '+active+'"><a class="page-link" herf="'+(endNum + 1 )+'">Next</a></li>';
		}
		
		str += "</ul></div>";
		
		console.log(str);
		
		replyPageFooter.html(str);
		
	}
	
	//페이지 클릭시
	replyPageFooter.on("click", "li a", function(e){
		e.preventDefault();
		
		console.log("page click!");
		
		var targetPageNum = $(this).attr("href");
		
		console.log("targetPageNum : " + targetPageNum);
		
		pageNum = targetPageNum;
		
		showList(pageNum);
		
	});
	
	//모달창 닫기
	$("#modalCloseBtn").on("click", function(){
		modal.modal("hide");
	});
	
	//첨부파일 목록 조회
	$.getJSON("/board/getAttachList", {bno : bnoValue}, function(arr){
		console.log("----------------");
		console.log(arr);
		
		var str = "";
		
		$(arr).each(function(i, attach){
			
			//image인 경우
			if(attach.fileType){
				var fileCallPath = encodeURIComponent( attach.uploadPath+"\\" + "s_" + attach.uuid + "_" + attach.fileName );	//파일경로
				
				str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-filetype='"+attach.fileType+"'>";
				str += "<div>";
				str += "<img src='/display?fileName="+fileCallPath+"'";
				str += "</div>";
				str += "</li>";
				
			}else{	//file인 경우
				str += "<li data-path='"+attach.uploadPath+"' data-uuid='"+attach.uuid+"' data-filename='"+attach.fileName+"' data-filetype='"+attach.fileType+"'>";
				str += "<div>";
				str += "<span>"+attach.fileName+"</span><br>";
				str += "<img src='/resources/img/attach.png'>";
				str += "</div>";
				str += "</li>";
			}
		});
		
		$(".uploadResult ul").html(str);
		
	})// end of getJSON
	
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
	
});	// end of $(document).ready

//원보이미지 보여주기
function showImage(fileCallPath){
	//alert(fileCallPath);
	
	/* display : flex; 속성은 이미지를 아래가 아닌 옆으로 조회해준다.  */
	$(".bigPictureWrapper").css("display","flex").show();
	
	/* animate() 속성  : 부드럽게 이미지를 보여줌(1초) */
	$(".bigPicture").html("<img src='/display?fileName="+fileCallPath+"'>").animate({width:'100%', height: '100%'}, 1000);
}

</script>
                            <!-- /.table-responsive -->
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->

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
 				<!-- 첨부파일 List조회 -->
 				<div class="uploadResult">
 					<ul>
 					</ul>
 				</div>
 			</div>
 		</div>
 	</div>
 </div>
 
<!-- 댓글달기 영역  -->     
<div class='row'>
	<div class="col-lg-12">
    	<div class="panel panel-default">
	        <div class="panel-heading">
	            <i class="fa fa-comments fa-fw"></i>Reply
	            <sec:authorize access="isAuthenticated()">
	            	<button id="addReplyBtn" class="btn btn-primary btn-xs pull-right">New Reply</button>
	            </sec:authorize>
	        </div>
	    </div>
	   	<div class="panel-body">
	   		<ul class="chat">
	   			<li class="left clearfix" data-rno='12'>
	   				<div>
	   					<div class="header">
	   						<strong class="primary-font">user00</strong>
	   						<small class="pull-rifht text-muted">2018-01-01 13:13</small>
	   					</div>
	   					<p>Good job!</p>
	   				</div>
	   			</li>
	   		</ul>
	   	</div>
		<!-- 댓글목록 paging 처리 	 -->
	   	<div class="panel-footer">
	   	</div> 
     </div>
     
</div>

<%@include file="../includes/footer.jsp" %>
<%@include file="../includes/modal.jsp" %>
	
	