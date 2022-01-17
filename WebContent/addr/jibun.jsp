<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=utf-8" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="ko" xml:lang="ko">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no" />
<title>지번검색</title>
<link type="text/css" href="./style.css" rel="stylesheet" />
<script src="./common.js"></script>
<script type="text/javascript">
/*
 *  ver  : 1.0
 *  date : 2012-08-07
 *  make : yjkim 
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
			objClear	= 'town1_oldaddr';		

			//①세종시예외처리 
			document.getElementById("county1").disabled = false;
			
			if(document.getElementById(obj.id).value =='36'){
				document.getElementById("county1").disabled = true;
				dataTo 		= 'town';
				objTo		= 'town1_oldaddr';
			}
		}
		// 통합검색 
		else if(obj.id == 'county1'){
			dataFrom 	= 'county';
			objFrom		= 'county1';
			objClear	= '';
			dataTo 		= 'town';
			objTo		= 'town1_oldaddr';
		}
		else if(obj.id == 'town1_oldaddr'){
			dataFrom 	= 'town';
			objFrom		= 'town1_oldaddr';
			objClear	= '';
			dataTo 		= 'ri';
			objTo		= 'ri1_oldaddr';
			
		}
		// 통합검색 
		else if(obj.id == 'rd_nm_idx1'){
			dataFrom 	= 'county';
			objFrom		= 'county1';
			objClear	= '';
			
		}

		//	update해야 할 select를 초기화
		clearList(objTo);
		
		//	clear해야 할 select가 설정된 경우 초기화
		
		// from object 선택값이 없으면 return
		if(document.getElementById(objFrom).value == ''){
			return;
		}
		
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
		var valFrom		= document.getElementById(objFrom).value;
		var valTo		= document.getElementById(objTo).value;

		//	시군구>도로명, 시군구>지번주소인 경우
		//	시도코드도 가져가야함

		if(dataFrom == 'county' && (areaIdx == 1 || areaIdx == 3)){
			var cityName = 'city'+areaIdx;
			var cityVal = document.getElementById(cityName).value;
		
			valFrom = cityVal+valFrom;
		
		}else if(dataFrom == 'town' && (areaIdx == 1 || areaIdx == 3)){
			var cityName	= 'city'+areaIdx;
			var countyName	= 'county'+areaIdx;

			if(document.getElementById(cityName).value=='36'){
				valFrom = '36110'+valFrom;
			}else
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
		if(form.bun1.value == ""){
			alert("번지를 입력하세요.");
			return;
		}
		
		var keyword = "";
		var cityText='',countyText='',townText='',riText='',orgCode='219',orgNm='국민생활체육회';
		var special_pattern = /['~!@#$%^&*|\\\'\'';:\.?]/gi;
		cityText= form.city1.options[form.city1.selectedIndex].text;
		countyText = form.county1.options[form.county1.selectedIndex].text;
		townText = form.town1_oldaddr.options[form.town1_oldaddr.selectedIndex].text;
		riText = form.ri1_oldaddr.options[form.ri1_oldaddr.selectedIndex].text;
		
		if(form.san.checked) 	form.san1.value = '1';
		else form.san1.value ='0';

		if(form.city1.value == "") cityText = "";
		if(form.county1.value == "") countyText = "";
		if(form.town1_oldaddr.value == "") townText = "";
		if(form.ri1_oldaddr.value == "") riText = "";
		
		form.engineCtpNm.value =cityText;
		form.engineSigNm.value =countyText;
		form.engineEmdNm.value =townText;
		form.engineLiNm.value = riText;
		form.engineBdMaSn.value= form.bun1.value;
		form.engineBdSbSn.value = form.bun2.value;
		form.engineMtYn.value= form.san1.value;
		form.currentPage.value =currentPage;

		if(cityText == ''){
			alert('시군구를 선택해 주세요');
			return;
		}

		var url;
		
		url = "./AjaxRequestXML.jsp?getUrl="+escape("http://125.60.46.141/link/search.do?extend=false&mode=jibun_search&searchType=location_jibun&topTab=1&engineCtpNm="+encodeURI(cityText)+"&engineSigNm="+encodeURI(countyText)+"&engineEmdNm="+encodeURI(townText)+"&engineLiNm="+encodeURI(riText)+"&engineBdMaSn="+encodeURI(form.bun1.value)+"&engineBdSbSn="+encodeURI(form.bun2.value)+"&engineMtYn="+encodeURI(form.san1.value)+"&currentPage="+currentPage+"&orgCode="+orgCode+"&orgNm="+encodeURI(orgNm));
		
		createXMLHttpRequest();
		
	 	xmlHttp.onreadystatechange = handleStateChangeSearch;
	  	xmlHttp.open("GET", url, true);
	  	xmlHttp.send(null);
	}

	function normalSearch2(currentPage){
		var url;
		var form = document.check;
		form.currentPage.value =currentPage;
		var orgCode='219',orgNm='국민생활체육회';

		currentPage=parseInt((currentPage/10)+1);
		
		url = "./AjaxRequestXML.jsp?getUrl="+escape("http://125.60.46.141/link/search.do?extend=false&mode=jibun_search&searchType=location_jibun&topTab=1&engineCtpNm="+encodeURI(form.engineCtpNm.value)+"&engineSigNm="+encodeURI(form.engineSigNm.value)+"&engineEmdNm="+encodeURI(form.engineEmdNm.value)+"&engineLiNm="+encodeURI(form.engineLiNm.value)+"&engineBdMaSn="+form.engineBdMaSn.value+"&engineBdSbSn="+form.engineBdSbSn.value+"&engineMtYn="+form.engineMtYn.value+"&currentPage="+currentPage+"&orgCode="+orgCode+"&orgNm="+encodeURI(orgNm));


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
			<img src="http://www.juso.go.kr/images/link/h1.jpg" alt="도로명주소찾기(우편번호)"/>
		</h1>
	</div>
	<div id="container">
		<div id="content">
			<ul class="tab">
				<li><a href="./road.jsp"><img src="http://www.juso.go.kr/images/link/tab1_off.gif" alt="도로명주소"/></a></li>
				<li><a href="./jibun.jsp"><img src="http://www.juso.go.kr/images/link/tab2.gif" alt="지번"/></a></li>
				<li><a href="./sangho.jsp"><img src="http://www.juso.go.kr/images/link/tab3_off.gif" alt="건물명"/></a></li>
			</ul>
		
				<form name="check" method="post">
					<input type="hidden" name="san1" id="san1"  value="0"/>
					<input type="hidden" name="engineCtpNm" id="engineCtpNm"  />
					<input type="hidden" name="engineSigNm" id="engineSigNm"  />
					<input type="hidden" name="engineEmdNm" id="engineEmdNm"  />
					<input type="hidden" name="engineLiNm" id="engineLiNm"  />
					<input type="hidden" name="engineMtYn" id="engineMtYn"  />
					<input type="hidden" name="engineBdMaSn" id="engineBdMaSn"  />
					<input type="hidden" name="engineBdSbSn" id="engineBdSbSn"  />
					<input type="hidden" name="currentPage" id="currentPage"   />
					
					<div class="form" id="formbox">
					<fieldset>
						<legend>정보 입력</legend>
						<div class="group">
						<p class="input">
							<label for="city1"><strong>&nbsp;&nbsp;&nbsp;&nbsp;시&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;도</strong></label>
							&nbsp;&nbsp;&nbsp;<select title="시/도 선택"  name="city1" onchange="javascript:changeAreaList(1, this);" id="city1" style="width:185px">
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
							<select title="시/군/구 선택"  name="county1" id="county1" style="width:185px" onchange="javascript:changeAreaList(1, this);">
							<option value="">::선택::</option>
							</select>
						</p>
						<p class="input">
							<label for="town1_oldaddr"><strong>읍&nbsp;&nbsp;면&nbsp;&nbsp;동</strong></label>
							<select id="town1_oldaddr" name="town1_oldaddr" title="지번주소" onchange="javascript:changeAreaList(1, this); " style="width:185px"> 
								<option value="">::선택::</option>
							</select>
						</p>
						<p class="input">
							<label for="ri1_oldaddr"><strong>리</strong></label>
							<select id="ri1_oldaddr" name="ri1_oldaddr" title="지번주소"  class="small" style="width:130px">  
								<option value="">::선택::</option>
							</select>
							<input type="checkbox" name="" value="" id="mut" name="san"/>
							<label for="mut">산</label>
						</p>
						<p class="input">
							<label for="bun1"><strong>번&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;지</strong></label>
							<input type="text" name="bun1" value="" id="bun1" class="text" style="width:84px"  onkeydown="checkKeyASearch();"/> - <input type="text" name="bun2" value="" id="bun2" class="text" style="width:84px"  onkeydown="checkKeyASearch();"/>
							
						 </p>
						</div>
						<div class="btn">
							<a href="javascript:normalSearch('1');return false;"><img src="http://www.juso.go.kr/images/link/btn_search.gif" alt="검색" onclick="normalSearch('1');return false;" /></a>
						</div>
						<div >
							<img src="http://www.juso.go.kr/images/test.gif" alt="올바른 도로명주소 표기법 / 공동주택(아파트 등)일때 서울특별시 서초구 반포대로58,101동 501호 (서초동, 서초아트자이) / 주택,상가 일때 서울특별시 서초구 반포대로23길 6(서초동)" width="380px" />
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
