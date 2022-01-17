<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=utf-8" %>

<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<title>도로명주소검색</title>
<link type="text/css" href="./style.css" rel="stylesheet" />
<script type="text/javascript" src="/lib/js/jquery-1.10.2.min.js"></script>
<script src="./common.js"></script>
<script type="text/javascript"> 
	/*
	 *	ajax사용 1 - 행정구역코드 리스트 변경(mode : area)
	 *
	 *	param : this object
	 */
   function changeAreaList(idx, obj){	
		if(obj.id == '' || obj.id == 'undefined')
			return;
		
		areaIdx = idx;
	
		// 통합검색 
		if(obj.id == 'city1'){
			dataFrom 	= 'city';
			dataTo 		= 'county';
			objFrom		= 'city1';
			objTo		= 'county1';
			//objClear	= 'town1_oldaddr';

			//①세종시예외처리 
			document.getElementById("county1").disabled = false;

			if(document.getElementById(obj.id).value =='36'){
				document.getElementById("county1").disabled = true;
			}		

			clearList(objTo);
			
		}
		// 통합검색 
		else if(obj.id == 'county1'){
			dataFrom 	= 'county';
			objFrom		= 'county1';
			objClear	= '';
			dataTo 		= '';
			objTo		= '';

			
		}
		else if(obj.id == 'town1_oldaddr'){
			dataFrom 	= 'town';
			objFrom		= 'town1_oldaddr';
			objClear	= '';
			//dataTo 		= 'ri';
			//objTo		= 'ri1_oldaddr';
			
		}
		// 통합검색 
		else if(obj.id == 'rd_nm_idx1'){
			dataFrom 	= 'county';
			objFrom		= 'county1';
			objClear	= '';
			
		}

		//	update해야 할 select를 초기화
			
		//	clear해야 할 select가 설정된 경우 초기화
		
		var url = "./AjaxRequestXML.jsp?getUrl=http://125.60.46.141/getAreaCode.do?" + escape(createParameter());
		
	  	createXMLHttpRequest();
	 	xmlHttp.onreadystatechange = handleStateChange;
	  	xmlHttp.open("GET", url, true);
	  	xmlHttp.send(null);
	}
	
	
	/*
	 *	행정구역 조회 쿼리에 사용할 파라메터값 설정
	 */
	function createParameter() {

		if(objFrom!='')
			valFrom		= document.getElementById(objFrom).value;
		if(objTo!='')
		valTo		= document.getElementById(objTo).value;

		//	시군구>도로명, 시군구>지번주소인 경우
		//	시도코드도 가져가야함
		if(dataFrom == 'county' && (areaIdx == 1 || areaIdx == 3)){
			var cityName = 'city'+areaIdx;
			var cityVal = document.getElementById(cityName).value;
			
			valFrom = cityVal+valFrom;
		}else if(dataFrom == 'town' && (areaIdx == 1 || areaIdx == 3)){
			var cityName	= 'city'+areaIdx;
			var countyName	= 'county'+areaIdx;
			
			valFrom = document.getElementById(cityName).value+document.getElementById(countyName).value+valFrom;
		}

		
		var queryString = "from="+encodeURI(dataFrom)+"&to="+encodeURI(dataTo)+"&valFrom="+encodeURI(valFrom)+"&valTo="+encodeURI(valTo);

	  	return queryString;
	}

	/*
	 *	지정된 행정구역코드 리스트를 초기화
	 */
	function clearList(obj) {
		if(obj == 'town1_oldaddr'){
			var toObject = document.getElementById(obj);

			toObject.options.length = 0;
			toObject.options[0] = new Option('::선택::', '');
			
			document.getElementById("ri1_oldaddr").options.length = 0;
			document.getElementById("ri1_oldaddr").options[0] = new Option('::선택::', '');
		}
		else if(obj != '' && obj != 'town1_oldaddr'){
			var toObject = document.getElementById(obj);
			  
			toObject.options.length = 0;
			toObject.options[0] = new Option('::선택::', '');
		}
	}
		
	function normalSearch(currentPage){
		var form = document.check;
		if(form.rd_nm1.value.length < 2) {
			alert("도로명을 2글자 이상 입력하세요!");
			form.rd_nm1.focus();
			return;
		}
		
		
		var keyword = "";
		var cityText='',countyText='',townText='',orgNm='국민생활체육회',orgCode='219';
		var special_pattern = /['~!@#$%^&*|\\\'\'';:\.?]/gi;
		cityText= form.city1.options[form.city1.selectedIndex].text;
		countyText = form.county1.options[form.county1.selectedIndex].text;
		
		
		if(form.city1.value == "") cityText = "";
		if(form.county1.value == "") countyText = "";
		
		form.engineCtpNm.value = cityText;
		form.engineSigNm.value = countyText;
		form.engineRdNm.value = trim(form.rd_nm1.value);
		form.engineBdMaSn.value = form.ma.value;
		form.engineBdSbSn.value = form.sb.value;
		form.currentPage.value = currentPage;		

		var url;

		url = "./AjaxRequestXML.jsp?getUrl="+escape("http://125.60.46.141/link/search.do?extend=true&mode=road_search&searchType=location_newaddr&topTab=1&engineCtpNm="+encodeURI(cityText)+"&engineSigNm="+encodeURI(countyText)+"&engineRdNm="+encodeURI(trim(form.rd_nm1.value))+"&engineBdMaSn="+encodeURI(form.ma.value)+"&engineBdSbSn="+encodeURI(form.sb.value)+"&currentPage="+currentPage+"&orgCode="+orgCode+"&orgNm="+encodeURI(orgNm));
		createXMLHttpRequest();
		
	 	xmlHttp.onreadystatechange = handleStateChangeSearch;
	  	xmlHttp.open("GET", url, true);
	  	xmlHttp.send(null);
	}

	function normalSearch2(currentPage){
		var url;
		var form = document.check;
		var orgNm='국민생활체육회',orgCode='219';
		form.currentPage.value =currentPage;
		currentPage=parseInt((currentPage/10)+1);
		url = "./AjaxRequestXML.jsp?getUrl="+escape("http://125.60.46.141/link/search.do?extend=true&mode=road_search&searchType=location_newaddr&topTab=1&engineCtpNm="+encodeURI(form.engineCtpNm.value)+"&engineSigNm="+encodeURI(form.engineSigNm.value)+"&engineRdNm="+encodeURI(trim(form.engineRdNm.value))+"&engineBdMaSn="+encodeURI(form.engineBdMaSn.value)+"&engineBdSbSn="+encodeURI(form.engineBdSbSn.value)+"&currentPage="+currentPage+"&orgCode="+orgCode+"&orgNm="+encodeURI(orgNm));
		
		createXMLHttpRequest();
		
	 	xmlHttp.onreadystatechange = handleStateChangeSearch;
	  	xmlHttp.open("GET", url, true);
	  	xmlHttp.send(null);
	}
</script>
</head>
<body>
<div id="wrapper">
	<div id="header">
		<h1>
			<img src="http://www.juso.go.kr/images/link/h1.jpg" alt="도로명주소찾기(우편번호)" />
		</h1>
	</div>
	<div id="container">
		<div id="content">
			<ul class="tab">
				<li><a href="./road.jsp" ><img src="http://www.juso.go.kr/images/link/tab1.gif" alt="도로명주소"/></a></li>
				<li><a href="./jibun.jsp" ><img src="http://www.juso.go.kr/images/link/tab2_off.gif" alt="지번"/></a></li>
				<li><a href="./sangho.jsp" ><img src="http://www.juso.go.kr/images/link/tab3_off.gif" alt="건물명"/></a></li>
			</ul>

				<form name="check" method="post">
					<input type="hidden" name="engineCtpNm" id="engineCtpNm"  />
					<input type="hidden" name="engineSigNm" id="engineSigNm"  />
					<input type="hidden" name="engineRdNm" id="engineRdNm"  />
					<input type="hidden" name="engineBdMaSn" id="engineBdMaSn"  />
					<input type="hidden" name="engineBdSbSn" id="engineBdSbSn"  />
					<input type="hidden" name="currentPage" id="currentPage"  />
					<input type="hidden" name="town1_oldaddr" id="town1_oldaddr"  />
					
					<div class="form" id="formbox">
					<fieldset>
						<legend>정보 입력</legend>
						<div class="group">
						<p class="input">
							<label for="city1"><strong>&nbsp;&nbsp;&nbsp;&nbsp;시&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;도</strong></label>
							&nbsp;&nbsp;&nbsp;<select title="시/도 선택"  name="city1" onchange="javascript:changeAreaList(1, this);" id="city1" style="width:193px">
									<option value="">::선택::</option>
									<option value="11" title="서울특별시" >서울특별시</option>
									<option value="42" title="강원도" >강원도</option>
									<option value="41" title="경기도">경기도</option>
									<option value="48" title="경상남도" >경상남도</option>
									<option value="47" title="경상북도" >경상북도</option>
									<option value="46" title="전라남도" >전라남도</option>
									<option value="45" title="전라북도" >전라북도</option>
									<option value="44" title="충청남도" >충청남도</option>
									<option value="43" title="충청북도" >충청북도</option>
									<option value="29" title="광주광역시" >광주광역시</option>
									<option value="27" title="대구광역시" >대구광역시</option>
									<option value="30" title="대전광역시" >대전광역시</option>
									<option value="26" title="부산광역시" >부산광역시</option>
									<option value="31" title="울산광역시" >울산광역시</option>
									<option value="28" title="인천광역시" >인천광역시</option>
									<option value="36" title="세종특별자치시" >세종특별자치시</option>
									<option value="50" title="제주특별자치도" >제주특별자치도</option>
							</select>
						</p>
						<p class="input">
							<label for="county1"><strong>시&nbsp;&nbsp;군&nbsp;&nbsp;구</strong></label>
							<select title="시/군/구 선택"  name="county1" id="county1" style="width:193px" >
							<option value="">::선택::</option>
							</select>
						</p>
						<p class="input">
							<label for="rd_nm1"><strong>도&nbsp;&nbsp;로&nbsp;&nbsp;명</strong></label>
							<input type="text" name="rd_nm1" id="rd_nm1" value="" class="text" style="width:193px;ime-mode:active" onkeydown="checkKeyASearch();"/>
						 </p>
						<p class="input">
							<label for="ma"><strong>건물번호</strong></label>
							<input type="text" name="ma" id="ma" class="text" style="width:90px" onkeypress="javascript:inputNumCom(event);" onkeydown="checkKeyASearch();"/> - <input type="text" name="sb" id="sb" class="text" style="width:90px" onkeypress="javascript:inputNumCom(event);" onkeydown="checkKeyASearch();" />
						 </p>
						 </div>
						<div class="btn">
							<a href="#"><img src="http://www.juso.go.kr/images/link/btn_search.gif" alt="검색" onclick="normalSearch('1');return false;" /></a>
						</div>
						<div >
							<img src="http://www.juso.go.kr/images/test.gif" width="380px" alt="올바른 도로명주소 표기법 / 공동주택(아파트 등)일때 서울특별시 서초구 반포대로58, 101동 501호 (서초동, 서초아트자이) / 주택,상가 일때 서울특별시 서초구 반포대로23길 6(서초동)" />
							자세한 표기법은   <a href="http://www.juso.go.kr" style="font-size: 10px" target="_blank">도로명주소안내홈페이지</a> 를 참조하세요
						</div>
					</fieldset>
				</div>
				</form>
			</div>
		</div>
	</div>

</body>
</html>
