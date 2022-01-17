var DEBUG = true;
function getJSON(url, data, func){
	$.ajax({"url" : url, "type": "POST", "data" : data, "dataType" : "json", cache : false}).done(func).fail(function(jqXHR,textStatus,errorThrown) {
		return; 
		alert("오류 발생 관리자에게 문의 바람");
		 alert(jqXHR.responseText);
		 if(DEBUG == false) return;
		 $("body").append(jqXHR.responseText);
	}); 
}

function addOptions(array, value, text, obj, defaultValue){
	var html = "";
	$.each(array, function(){
		obj.empty();
		html += "<option value='" + this[value] + "' ";
		if(this[value] == defaultValue)
			html += "selected='selected'";
		html += ">" + this[text] + "</option>";
	});
	obj.append(html);
}

function selectValInput(id, dialog, text, value)
{ 
	jQuery("#"+id).val(text);
	//jQuery("#uppart").val(value);
	jQuery("#carpart").val(value);
	jQuery("#" + dialog).dialog( "close" );
}

function trim(value) {
	value = value.replace(/\s/g, "");
    return value;
} 

function addList(array, value, text, obj, defaultValue){
	//var html = "<tr><th>부품명</th></tr>";
	var html = "";
	$.each(array, function(){
		obj.empty();
		html += "<tr><td>";
		html += "<a onclick = selectValInput('uppartnm','dialog3','" + trim(this[text]) + "','" + trim(this[value]) + "');>" + this[text] + "</a>";
	
		html += "</td></tr>";
	});
	obj.append(html);
}

function addOptions_defaultNm(array, value, text, obj, defaultValue, defaultNm){
	var html = "";
	$.each(array, function(){
		html += "<option value='" + this[value] + "' ";
		if(this[value] == defaultValue)
			html += "selected='selected'";
		html += ">" + this[text] + defaultNm+"</option>";
	});
	obj.append(html);
}

function addChecbox(array, name, value, text, obj, defaultValue){
	var html = "";
	$.each(array, function(idx){
		html += "<label><input type='checkbox' name='" + name + "' value='" + this[value] + "' ";
		if(defaultValue.indexOf(this[value]) > -1)
			html += "checked='checked'";
		html += " />" + this[text] + "</label>&nbsp;";
		if(idx > 0 && (idx + 1)%5 == 0) html += "<br />";
	});
	obj.append(html);
}



function setFormData(f, data){
	for(key in data){
		if (data.hasOwnProperty(key) == false) continue;
		
		$("[name=" + key + "]", f).each(function(){
			if(this.tagName.toLowerCase() == "textarea" || "radio,checkbox".indexOf(this.type.toLowerCase()) == -1){
				this.value = data[key];
				return;
			}
			
			this.checked = String(data[key]).indexOf(this.value) > -1;
		});
	}
}


function openAttachDialog(attach_cnt, fn, exts, width, image_yn){
	var html = "";
	for(var i = 0; i < attach_cnt; i++){
		html += "<input type='file' name='file' /><br />";
	}
	
	var f = $("#attachWrap form");
	if(image_yn == undefined || image_yn == "N")
		f.attr("action","/upload.do");
	else
		f.attr("action","/mainImageUpload.do");
	
	$("[type=file]", f).remove();
	$("br", f).remove();
	f.prepend(html);
	var bar = $('.bar');
	var percent = $('.percent');
	f.ajaxForm({
		dataType : "json"
		,beforeSubmit : function(){
			var checked = true;
			$("[type=file]", f).each(function(){
				if(exts == "") return;
				if(exts.toLowerCase().indexOf(this.value.substring(this.value.lastIndexOf(".") + 1).toLowerCase() ) > -1)
					return;
				
				checked = false;
			});
			
			if(checked == false){
				alert(exts + " 파일로 선택해주세요");
				return checked;
			}
			
			if(width == "" || width == undefined || width <= 0)
				width = 500;
			
			$("[name=width]", f).val(width);
			
			var percentVal = '0%';
	        bar.width(percentVal);
	        percent.html(percentVal);
			return checked;
		},
		uploadProgress: function(event, position, total, percentComplete) {
	        var percentVal = percentComplete + '%';
	        bar.width(percentVal);
	        percent.html(percentVal);
	    }
		,success : fn
		,complete : function(){
			$("#attachWrap").dialog("close");
		}
	});
	
	$("#attachWrap").dialog({
		height : "200"
		,width : "550"
	});
	return false;
}

//긴문자열 ...처리
$.cutString = function(str, size) {
	if($.checkByte(str) > size){
		var result = "";
		for(var i=0, j=0; j<size; i++, j++){
			if(str.charAt(i) >= ' ' && str.charAt(i) <= '~'){;}			
			else {j++;}
			result += str.charAt(i);
		}
		return result + "...";
	}else{
		return str;
	}
};

//byte수 가져오기
$.checkByte = function(str) {
	var strByte=0;
	for(var i=0; i<str.length; i++){
		if(str.charAt(i) >= ' ' && str.charAt(i) <= '~' )
			strByte++;
		else
			strByte += 2;
	}
	return strByte;
};


Map = function() {
	this.map = new Object();
};
Map.prototype = {
	put : function(key, value) {		
		this.map[key] = value;
	},
	get : function(key) {
		return this.map[key];
	},
	containsKey : function(key) {
		return key in this.map;
	}
};

function stringSplit(strData, separator) {
	var stringList = new Array();
	while (strData.indexOf(separator) != -1) {
		stringList[stringList.length] = strData.substring(0, strData
				.indexOf(separator));
		strData = strData.substring(strData.indexOf(separator)
				+ (separator.length), strData.length);
	}
	stringList[stringList.length] = strData;
	return stringList;
}


if (typeof String.prototype.startsWith != 'function') {
	  String.prototype.startsWith = function (str){
		return this.slice(0, str.length) == str;
	  };
	}

if (typeof String.prototype.endsWith != 'function') {
	  String.prototype.endsWith = function (str){
		return this.slice(-str.length) == str;
	  };
	}

function openAddr(){
	window.open("/lib/addr/road.jsp","addr","width=450,height=680");
	return false;
}

function drawList(selector, data){
	var listHTML = $("body").data(selector);
	if(listHTML == undefined){
		listHTML = $(selector).html();
		$("body").data(selector,listHTML);
	}

	if(data.length == 0){
		$(selector).html("<tr><td colspan='" + $("td", listHTML).length + "'>검색결과가 없습니다.</td></tr>");
		$(selector).show();
		return;
	}
	
	var temp;
	var html = "";
	$.each(data, function(index, element){
		temp = listHTML;
		for(key in element){
			if (element.hasOwnProperty(key) == false) continue;
			temp = temp.replace(eval("/article#" + key + "#/ig"),element[key]);
		}

		html += temp;
	});
	$(selector).html(html);
	$(selector).show();
}

function drawPaging(selector, cpage, data){
	var pagingHTML = $("body").data(selector);
	if(pagingHTML == undefined){
		pagingHTML = $(selector).html();
		$("body").data(selector,pagingHTML);
	}
	cpage = Number(cpage);
	var temp;
	var start = Math.floor( (cpage - 1) / 10) * 10 + 1;
	var end = start + 9 < data.totalpage? start + 9: data.totalpage;
	
	var html = "";
	temp = pagingHTML;
	var temp2 = temp.split("#page#");
	if(start - 1 > 0){
		html += temp2[0] + (start - 1) + temp2[1] + "&lt;&lt;" + temp2[2];
	}
	if(cpage > 1){
		html += temp2[0] + (cpage - 1) + temp2[1] + "&lt;" + temp2[2];
	}
	for(var i = start; i <= end; i++){
		if(i != cpage){
			html += temp.replace(/#page#/ig,i);
			continue;
		}
		
		html += "<b>" + temp.replace(/#page#/ig,i) + "</b>";
	}
	if(cpage < data.totalpage){
		html += temp2[0] + (cpage + 1) + temp2[1] + "&gt;" + temp2[2];
	}
	if(end < data.totalpage){
		html += temp2[0] + (end + 1) + temp2[1] + "&gt;&gt;" + temp2[2];
	}
	$(selector).html(html);
	$(selector).show();
}

function checkTextAreaLength(d, cnt, comment, length) {
	/**
	 * 목적 : 참고사항 글자수 체크
	 * 매개변수 : 없음
	 * 반환값 : 없음
	 * 개정이력 : 없음
	 */
	$('#'+d).each(function(){
	    var $count = $('#'+cnt, this);
	    var $input = $("#"+comment);
	    var maximumCount = $count.text() * 1;
	    
	    var update = function(){
	        var before = $count.text() * 1;
	        var now = maximumCount - $input.val().length;
	        
	        // 사용자가 입력한 값이 제한 값을 초과하는지를 검사한다.
	        if (now < 0) {
	            var str = $input.val();
	            $("#"+comment).focus();
	            var inputVal = str.substr(0, maximumCount);
	            alert(length+'자를 초과하였습니다.');
	            now = 0;
	            $input.val(inputVal);
	        }
	        
	        // 필요한 경우 DOM을 수정한다.
	        if (before != now) {
	            $count.text(now);
	        }
	    };
	    $input.bind('input keyup paste', function(){
	        setTimeout(update, 0);
	    });
	    update();
	    
	});
}



function totalSearch(f){
	if(f.keyword.value == ""){
		alert("검색어를 입력해주세요");
		return false;
	}

	if(f.keyword.value.length < 2){
		alert("검색 단어를 2글자 이상 입력해주세요");
		return false;
	}

	return true;
}


function copy_trackback(trb) {
var IE=(document.all)?true:false;
if (IE) {
if(confirm("이 글의 트랙백 주소를 클립보드에 복사하시겠습니까?"))
window.clipboardData.setData("Text", trb);
} else {
temp = prompt("이 글의 트랙백 주소입니다. Ctrl+C를 눌러 클립보드로 복사하세요", trb);
}
}

function pagePrint(Obj) { 
    var W = Obj.offsetWidth;        //screen.availWidth; 
    var H = Obj.offsetHeight;       //screen.availHeight;

    var features = "menubar=no,toolbar=no,location=no,directories=no,status=no,scrollbars=yes,resizable=yes,width=" + W + ",height=" + H + ",left=0,top=0"; 
    var PrintPage = window.open("about:blank",Obj.id,features); 

    PrintPage.document.open(); 
    PrintPage.document.write("<html><head><title>인쇄 미리보기</title><link href='/lib/css/sub.css' rel='stylesheet' type='text/css' /><link href='/lib/css/common.css' rel='stylesheet' type='text/css' /><link href='/lib/css/sub.css' rel='stylesheet' type='text/css' /><link href='/lib/css/article_board.css' rel='stylesheet' type='text/css' /><link href='/lib/css/article_common.css' rel='stylesheet' type='text/css' /><link href='/lib/css/article_sub.css' rel='stylesheet' type='text/css' />\n</head>\n<body><div id='article' style='padding-bottom:40px;'>" + Obj.innerHTML + "</div>\n</body></html>"); 
    PrintPage.document.close(); 

    PrintPage.document.title = document.domain; 
    PrintPage.print(PrintPage.location.reload());
    PrintPage.print(PrintPage.location.reload());
}
