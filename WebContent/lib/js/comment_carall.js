
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
	html += '<div>';
    html += '<span class="textarea"><textarea class="textarea_1" name="conts">'+text+'</textarea></span>';
    html += '<a href="javascript:void(0);" class="comm_btn" onclick="commentReply(document.commentUpdate'+seq+');"><img src="/images/'+imgsrc+'article/comm_btn_en.gif" alt="입력하기"></a>';
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
		if(this.password != null){
			html += this.reg_nm+'</strong> <span>'+this.reg_dt+'</span></p>';
		}else{
			if(this.seq == null){
				html += this.reg_id+'</strong> <span>'+this.reg_dt+'</span></p>';
			}else{
				html += '<a href="javascript:openMap_m(\''+this.seq+'\');">'+this.reg_nm+'<img src="/images/common/info.gif" alt="업체정보보기" style="height:20px;margin-top:-3px;"/></a></strong> <span>'+this.reg_dt+'</span></p>';
			}
		}
	    if(boolean == "true"){
	    	html += '<p class="ct_r">';
	    	if(memberid == this.reg_id || this.password != null){
	    		//html += '<a href="#comment'+this.comment_seq+'" onclick="commentDel(\''+this.comment_seq+'\')"><img src="/images/article/comm_btn_1.png" alt="삭제"></a> ';
	    		//html += '<a href="#comment'+this.comment_seq+'" onclick="commentUpdate(\''+this.comment_seq+'\',\''+imgsrc+'\')"><img src="/images/article/comm_btn_2.png" alt="수정"></a> ';
	    	}	      
	      	html += '<a href="#comment'+this.comment_seq+'" onclick="commentReplyOnOff(\''+this.comment_seq+'\',\'open\')"><img src="/images/article/comm_btn_4.png" alt="답글"></a> ';
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
		    if(memberid == "unknown"){
		    	html += '이름 : <input type="text" name="member_nm" value="" style="width:100px;"/>';
		    	html += '<input type="hidden" name="password" value="." style="width:100px;"/>';
		    	memberid = "";
		    }
		    if(this.password == null){
		    	html += '<p><strong class="comm_arrow">'+memberid+' @'+this.reg_id+' 님께 댓글쓰기</strong> <a class="mod" href="#comment'+this.comment_seq+'" onclick="commentReplyOnOff(\''+this.comment_seq+'\',\'close\')><img src="/images/article/comm_btn_3.png" alt="취소"></a></p>';
		    }else{
		    	html += '<p><strong class="comm_arrow">'+memberid+' @'+this.reg_nm+' 님께 댓글쓰기</strong> <a class="mod" href="#comment'+this.comment_seq+'" onclick="commentReplyOnOff(\''+this.comment_seq+'\',\'close\')><img src="/images/article/comm_btn_3.png" alt="취소"></a></p>';
		    }
		    html += '<div>';
		    html += '<span class="textarea"><textarea class="textarea_1" name="conts"></textarea></span>';
		    html += '<a href="javascript:void(0);" class="comm_btn" onclick="commentReply(document.comment'+this.comment_seq+');"><img src="/images/'+imgsrc+'article/comm_btn_en.gif" alt="입력하기"></a>';
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
    		+'이름 : <input type="text" name="member_nm" value="" style="width:100px;"/>'
    		+'<input type="hidden" name="password" value="." style="width:100px;"/>'
    		+'<input type="hidden" name="mode" value="commentInsert"/>'
    		+'<input type="hidden" name="article_seq" value="'+article_seq+'"/>'
    		+'<div>'
    		+'<span class="textarea"><textarea class="textarea_1" name="conts"></textarea></span>'
    		+'<a href="javascript:void(0);" class="comm_btn" onclick="commentReply(document.comment);"><img src="/images/'+imgsrc+'article/comm_btn_en.gif" alt="입력하기"></a>'
    		+'</div>'
    		+'</form>'
    		+'</div>'
    		+'</li>';
    	obj.html(html);
}

function openMap(seq){
	$("#modal_dialog").load("/popup/carallbaro/index.do", {seq : seq});
	$("#modal_dialog").dialog({title:'업체정보',height:600,width:590}).dialog("open");
}

function openMap_m(seq){
	var ngWidth = ($(window).width() * 0.9);
	var ngHeight = ($(window).height() * 0.9);
	if(ngHeight == 0 ){
		ngHeight = 300;
	}
	$("#modal_dialog").load("/popup/carallbaro/index.do?mobile=y", {seq : seq});
	$("#modal_dialog").dialog({title:'업체정보',height:ngHeight,width:ngWidth}).dialog("open");
}