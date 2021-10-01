<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
                              <button type="button" class="btn btn-default modBtn"><a href="/board/modify?bno=<c:out value='${board.bno }' />">modify</a></button>
                           
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
	
	//댓글등록 모달창 show
	$("#addReplyBtn").on("click", function(){
		modal.find("input").val("");	//input 값 초기화
		modalInputReplyDate.closest("div").hide(); //replyDate에 가장 가까운 div 숨기기
		modal.find("button[id != 'modalCloseBtn']").hide(); //닫기버튼이 아닌 것만 숨기기
		
		modalRegisterBtn.show();	//등록버튼 보여주기
		
		$(".modal").modal("show");	//모달창 show
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
		var reply = {
				rno : modal.data("rno"),
				reply : modalInputReply.val()
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
		
		replyService.remove(rno, function(result){
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
	
	
	
});
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
            
<div class='row'>
	<div class="col-lg-12">
    	<div class="panel panel-default">
	        <div class="panel-heading">
	            <i class="fa fa-comments fa-fw"></i>Reply
	            <button id="addReplyBtn" class="btn btn-primary btn-xs pull-right">New Reply</button>
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
	
	