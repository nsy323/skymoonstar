/**
 * 
 */

console.log("Reply Module......");

var replyService = (function(){
	
	//댓글 등록
	function add(reply, callback, error){
		console.log("add reply........");
		
		$.ajax({
			type : 'post',
			url : '/replies/new',
			data : JSON.stringify(reply),
			contentType : "application/json; charset=utf-8",
			success : function(result, status, xhr){
				if(callback){
					callback(result);
				}
			},
			error : function(xhr, status, er){
				if(error){
					error(er);
				}
			}
		})
	}
	
	//댓글목록 조회
	function getList(param, callback, error){
		var bno = param.bno;
		var page = param.page || 1;
		
		$.getJSON("/replies/pages/" + bno + "/" + page + ".json",
				function(data){
					if(callback){
						callback(data);
					}
				}).fail(function(xhr, status, err){
					if(error){
						error();
					}
				});
	}
	
	//댓글 삭제
	function remove(rno, callback, error){
		
		$.ajax({
			type : 'delete',
			url : '/replies/' + rno,
			success : function(deleteResult, status, xhr){
							
				if(callback){
					callback(deleteResult);
				}
			},error : function(xhr, status, er){
				if(error){
					error(er);
				}
			}
		});
	}
	
	//댓글 수정
	function modify(reply, callback, error){
		console.log("rno : " + reply.rno);
		
		$.ajax({
			type : 'put',
			url : '/replies/' + reply.rno,
			data : JSON.stringify(reply),
			contentType : "application/json; charset=utf-8",
			success : function(result, status, xhr){
				console.log("result : " + result);
				console.log("staus : " + status);
				console.log("xhr : " + xhr);
				
				if(callback){
					callback(result);
				}
				
			},error : function(xhr, status, er){
				console.log("xhr : " + xhr);
				console.log("staus : " + status);
				console.log("er : " + er);
				if(error){
					error(er);
				}
			} 
		});
		
	}
	
	//댓글 조회
	function get(rno, callback, error){
		
		$.get("/replies/" + rno + ".json", function(result){
			if(callback){
				callback(result);
			}
		}).fail(function(xhr, status, err){
			if(error){
				error();
			}
		});
	}
	
	return {
			add : add,
			getList : getList,
			remove : remove,
			modify : modify,
			get : get
	};
	
})();