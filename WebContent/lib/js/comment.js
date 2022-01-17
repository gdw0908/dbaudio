
function commentReplyOnOff(seq,val){
	if(val == "close"){
		$("#commentReply"+seq).css("display","none");
	}else{
		$("#commentReply"+seq).css("display","block");
	}	
}

function commentDel(seq){
	if(confirm("삭제하시겠습니까?")){
		var f = document.commentDelete;
		f.comment_seq.value = seq;
		commentReply(f);
		return false;
	}
}

function commentUpdate(seq,imgsrc){
	var text = $.trim($("#comm_text"+seq).text());
	if(text.indexOf('@') == 0){
		text = text.substring(text.indexOf(' '), text.length);
		text = $.trim(text);
	}
	var html = '';
	html += '<div class="coment_wrap">';
    html += '<textarea class="textarea" name="conts">'+text+'</textarea>';
    html += '<a href="#" class="comm_btn" onclick="commentReply(document.commentUpdate'+seq+');">입력하기</a>';
    html += '</div>';
    $("#comm_text"+seq).attr("class","area_box");
    $("#comm_text"+seq).html(html);
}

function commentReply(f){
	getJSON(f.action, $(f).serialize(), function(data) {
		if(data.boolean == "false"){
			if(data.rst == "2"){
				alert("권한이 없습니다.");
			}else{
				alert("로그인 후 이용하시기 바랍니다.");
			}
			return false;
		}
		commentTreeDraw(data,data.boolean,$("#comment_list"));
	});
	return false;
}

function commentTreeDraw(scope, boolean, obj){
	var html = "";
	
	var data = scope.comment;
	var imgsrc = scope.img;
	var memberid = scope.session_id;
	var servletPath = scope.servletPath;
	var article_seq = scope.article_seq
	$.each(data,function(){
		html += '<li';
		if(this.lvl > 0){
			html += ' class="comm_2"'
		}
		html += ' id="comment'+this.comment_seq+'">';
		html += '<div class="comment_top">';
		html += '<p class="ct_l"><strong';
		if(this.lvl > 0){
			html += ' class="comm_arrow"';
		}
		html += '>';
		html += this.reg_id+'</strong> <span>'+this.reg_dt+'</span></p>';
	    if(boolean == "true"){
	    	html += '<p class="ct_r">';
	    	if(memberid == this.reg_id){
	    		html += '<a href="#comment'+this.comment_seq+'" onclick="commentDel(\''+this.comment_seq+'\')">삭제</a> ';
	    		html += '<a href="#comment'+this.comment_seq+'" onclick="commentUpdate(\''+this.comment_seq+'\',\''+imgsrc+'\')">수정</a> ';
	    	}	      
	      	html += '<a href="#comment'+this.comment_seq+'" onclick="commentReplyOnOff(\''+this.comment_seq+'\',\'open\')">답글</a> ';
	      	html += '</p>';
	    }
	    html += '</div>';

	    html += '<form name="commentUpdate'+this.comment_seq+'" action="'+servletPath+'" method="post" onsubmit="return commentReply(this);">';
	    html += '<input type="hidden" name="comment_seq" value="'+this.comment_seq+'"/>';
	    html += '<input type="hidden" name="mode" value="commentReplyUpdate"/>';
	    html += '<div class="comm_text" id="comm_text'+this.comment_seq+'">';
	    html += '<p id="conts'+this.comment_seq+'">';
	    if(this.lvl > 0){
	    	html += '<strong>@'+this.reply_id+'</strong> ';	
	    } 
	    html += this.conts;
	    html += '</p>'
	    html += '</div>';
	    html += '</form>';
	    
	    
	    
	    if(boolean == "true"){
		    html += '<div class="area_box" id="commentReply'+this.comment_seq+'" style="display:none;"><!-- 댓글 로그인 사용자만 -->';
		    html += '<form name="comment'+this.comment_seq+'" action="'+servletPath+'" method="post" onsubmit="return commentReply(this);">';
		    html += '<input type="hidden" name="reply_id" value="'+this.reg_id+'"/>';
		    html += '<input type="hidden" name="comment_seq" value="'+this.comment_seq+'"/>';
		    html += '<input type="hidden" name="mode" value="commentReply"/>';
		    html += '<p><strong class="comm_arrow">'+memberid+' @'+this.reg_id+' 님께 댓글쓰기</strong> <a class="mod" href="#comment'+this.comment_seq+'" onclick="commentReplyOnOff(\''+this.comment_seq+'\',\'close\')><img src="/images/article/comm_btn_3.png" alt="취소"></a></p>';
		    html += '<div class="coment_wrap">';
		    html += '<textarea class="textarea" name="conts"></textarea>';
		    html += '<a href="#" class="comm_btn" onclick="commentReply(document.comment'+this.comment_seq+');">입력하기</a>';
		    html += '</div>';
		    html += '</form>';
		    html += '</div>';
		    html += '</li>';
	    }
	});
	

    //<!-- 댓글 원글 달기 -->
    	html += '<li>'
    		+'<div class="area_box area_box_2">'
    		+'<p class="comm_txt">댓글은 <b>1000</b> 자 내외로 작성해주시기 바랍니다.</p>'
    		+'<form name="comment" action="'+servletPath+'" method="post" onsubmit="return commentReply(this);">'
    		+'<input type="hidden" name="mode" value="commentInsert"/>'
    		+'<input type="hidden" name="article_seq" value="'+article_seq+'"/>'
    		+'<div class="coment_wrap">'
    		+'<textarea class="textarea" name="conts"></textarea>'
    		+'<a href="#" class="comm_btn" onclick="commentReply(document.comment);">입력하기</a>'
    		+'</div>'
    		+'</form>'
    		+'</div>'
    		+'</li>';
    	obj.html(html);
}