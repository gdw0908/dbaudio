function goAgree(){
	var f = document.articleSearchForm;
	f.mode.value = "agree";
	f.submit();
	return false;
}

function goInsertForm(){
	var f = document.articleSearchForm;
	f.mode.value = "insertForm";
	f.submit();
	return false;
}

function goPage(cpage){
	var f = document.articleSearchForm;
	f.mode.value = "list";
	f.cpage.value = cpage;
	f.submit();
	return false;
}

function goPageRows(cpage){
	var f = document.articleSearchFormRows;
	f.mode.value = "list";
	f.cpage.value = cpage;
	f.submit();
	return false;
}

function pageRows(rows){
	var f = document.articleSearchForm;
	f.mode.value = "list";
	f.rows.value = rows;
	f.submit();
	return false;
}


function articleInsert(f){
	
	if(f.reg_nm != null && f.reg_nm.value == ""){
		alert("작성자를 입력하세요");
		f.reg_nm.focus();
		return false;
	}
	
	//alert(CKEDITOR.instances.conts.getData());
	/*if ( CKEDITOR.instances.conts.getData() == '' ){
	    alert( 'There is no data available' );
	}*/
	
	if(f.thumb.value != null){
		try{
			var img_array = $(CKEDITOR.instances.conts.document.$.body).find('img');
			f.thumb.value = "";
			var domain = "http://" + document.domain;
			$.each(img_array, function(idx){
				if(f.thumb.value != "") return;
				if(this.src.indexOf(domain) == -1) return;
				f.thumb.value = this.src.substring(domain.length);
				return false;
			});
		}catch(e){
			;
		}
	}
	return confirm("게시물을 등록하시겠습니까?");
}

function articleUpdate(f){
	if(f.title.value == ""){
		alert("제목을 입력하세요");
		f.title.focus();
		return false;
	}
	
	if(f.thumb.value != null){
		try{
			var img_array = $(CKEDITOR.instances.conts.document.$.body).find('img');
			f.thumb.value = "";
			var domain = "http://" + document.domain;
			$.each(img_array, function(idx){
				if(f.thumb.value != "") return;
				if(this.src.indexOf(domain) == -1) return;
				f.thumb.value = this.src.substring(domain.length);
				return false;
			});
		}catch(e){
			;
		}
	}
	
	return confirm("게시물을 수정하시겠습니까?");
}


function articleInsertForEbook(f){
	if(f.title.value == ""){
		alert("제목을 입력하세요");
		f.title.focus();
		return false;
	}
	
	if(f.attach[0].value == ""){
		alert("썸네일 이미지를 첨부하세요");
		f.attach[0].focus();
		return false;
	}
	
	if(f.attach[1].value == ""){
		alert("e-book을 첨부하세요");
		f.attach[1].focus();
		return false;
	}
	return confirm("게시물을 등록하시겠습니까?");
}

function articleUpdateForEbook(f){
	if(f.title.value == ""){
		alert("제목을 입력하세요");
		f.title.focus();
		return false;
	}
	
	if(f.attach[0].value != "" || f.attach[1].value != ""){
		if(f.attach[0].value == ""){
			alert("썸네일 이미지를 첨부하세요");
			f.attach[0].focus();
			return false;
		}
		
		if(f.attach[1].value == ""){
			alert("e-book을 첨부하세요");
			f.attach[1].focus();
			return false;
		}
	}
	return confirm("게시물을 수정하시겠습니까?");
}

function articleInsertForImageListDownload(f){
	if(f.title.value == ""){
		alert("제목을 입력하세요");
		f.title.focus();
		return false;
	}
	
	if(f.attach[0].value == ""){
		alert("이미지를 첨부하세요");
		f.attach[0].focus();
		return false;
	}
	
	if(f.attach[1].value == ""){
		alert("첨부파일을 첨부하세요");
		f.attach[1].focus();
		return false;
	}
	return confirm("게시물을 등록하시겠습니까?");
}

function articleUpdateForImageListDownload(f){
	if(f.title.value == ""){
		alert("제목을 입력하세요");
		f.title.focus();
		return false;
	}
	
	if(f.attach[0].value != "" || f.attach[1].value != ""){
		if(f.attach[0].value == ""){
			alert("이미지를 첨부하세요");
			f.attach[0].focus();
			return false;
		}
		
		if(f.attach[1].value == ""){
			alert("첨부파일을 첨부하세요");
			f.attach[1].focus();
			return false;
		}
	}
	return confirm("게시물을 수정하시겠습니까?");
}

function articleInsertForImageListLink(f){
	if(f.title.value == ""){
		alert("제목을 입력하세요");
		f.title.focus();
		return false;
	}
	
	if(f.attac.value == ""){
		alert("이미지를 첨부하세요");
		f.attach.focus();
		return false;
	}
	
	if(f.conts.value == ""){
		alert("링크 주소를 입력하세요");
		f.conts.focus();
		return false;
	}
	return confirm("게시물을 등록하시겠습니까?");
}

function articleUpdateForImageListLink(f){
	if(f.title.value == ""){
		alert("제목을 입력하세요");
		f.title.focus();
		return false;
	}
	
	if(f.attac.value == ""){
		alert("이미지를 첨부하세요");
		f.attach.focus();
		return false;
	}
	
	if(f.conts.value == ""){
		alert("링크 주소를 입력하세요");
		f.conts.focus();
		return false;
	}
	return confirm("게시물을 수정하시겠습니까?");
}

function articleReply(f){
	if(f.title.value == ""){
		alert("제목을 입력하세요");
		f.title.focus();
		return false;
	}
	return confirm("게시물을 등록하시겠습니까?");
}

function remove(article_seq){
	var f = document.articleSearchForm;
	f.article_seq.value = article_seq;
	f.mode.value = "removeArticles";
	f.cpage.value = 1;
	f.submit();
	return false;
}

function remove_u(){
	
	if(confirm("게시물을 삭제하시겠습니까?") == false){
		return false;
	}
	
	var f = document.articleSearchForm;
	f.mode.value = "remove";
	f.submit();
	return false;
}

function remove(){
	var article_seq = new Array();
	$("input[name=article_seq_checkbox]:checked").each(function(){
		article_seq.push(this.value);
	});
	
	if(article_seq.length == 0){
		alert("게시물을 선택해주세요.");
		return false;
	}
	
	if(confirm("게시물을 삭제하시겠습니까?") == false){
		return false;
	}
	
	var f = document.articleSearchForm;
	f.article_seq.value = article_seq.join(",");
	f.mode.value = "remove";
	f.cpage.value = 1;
	f.submit();
	return false;
}

function goUpdateForm(article_seq){
	var f = document.articleSearchForm;
	f.article_seq.value = article_seq;
	f.mode.value = "updateForm";
	f.submit();
	return false;
}

function goView(article_seq){
	var f = document.articleSearchForm;
	f.article_seq.value = article_seq;
	f.mode.value = "view";
	f.submit();
	return false;
}

function goReplyForm(article_seq){
	var f = document.articleSearchForm;
	f.article_seq.value = article_seq;
	f.mode.value = "replyForm";
	f.submit();
	return false;
}

/*function addAttach(limit){
	if(!!limit){
		var cnt = $("div.attachWrap").children("div").size();
		if(cnt>=limit){
			alert("최대 " + limit + " 파일까지만 올리실수 있습니다.");
			return false;
		}
	}
	initAttachHTML();
	var html = $("body").data("attachHTML");
	$("div.attachWrap").append(html);
	return false;
}*/

function addAttach(){
	initAttachHTML();
	var html = $("body").data("attachHTML");
	$("div.attachWrap").append(html);
	fileLimitCheck();
	return false;
}

function fileLimitCheck(limit_file_size){
	$('input[name=attach]').change(function(){
		
		if($(this).attr('class') != null && $(this).attr('class').indexOf("image") > -1){
			var str = $(this).val();
			var checkExt = "jpg|jpeg|gif|bmp|png";
			if(str == "") return true;
			var dotIndex = str.lastIndexOf(".");
			var ext = str.substring(dotIndex+1).toLowerCase();
			var pattern = eval("/^(" + checkExt.toLowerCase() + "){1}$/");
			if(ext.search(pattern) == -1){
				alert("이미지 파일("+checkExt+")을 첨부해 주세요." );
				$(this).val("");
				return;
			}
		}
		
		var f = this.files[0]
		var flag = false;
		var limitMB = limit_file_size;
		var resultMB	= 0;
		
		if(f!=undefined)
			var iSize = (f.size||f.fileSize); 
		else
			return;
		
		resultMB = Math.floor(((iSize/1024)/1024));
		if(resultMB > limitMB){
			alert("첨부파일은 "+limitMB+"MB 이하로 업로드 가능합니다." );
			$(this).val("");
			return;
		}
	});
}

function initAttachHTML(){
	var html = $("body").data("attachHTML");
	if(html == undefined || html == ""){
		$("body").data("attachHTML", $("div.attachWrap").html());
	}
}
function removeAttach_(obj){
	initAttachHTML();
	$(obj).parent().remove();
	return false;
}

function removeAttach(uuid){
	if(confirm("첨부파일을 삭제하시겠습니까?") == false) return false;
	var f = document.articleSearchForm;
	f.mode.value="attachRemove";
	f.uuid.value = uuid;
	f.submit();
	return false;
}

function viewAnswer(article_seq){
	$('.answer_tr:visible').hide();
	$('#answer_tr_'+article_seq).show();
}

function goVill(boardvillid){
	alert(boardvillid);
}
