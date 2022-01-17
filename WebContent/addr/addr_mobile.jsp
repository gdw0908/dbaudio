<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
		<div class="add_search">
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
							<label for="city1"><strong><b>*</b>시도</strong></label>
							<span><select title="시/도 선택"  name="city1" onchange="javascript:changeAreaList(1, this);" id="city1" class="add_box">
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
							</span>
						</p>
						
						<p class="input">
							<label for="county1"><strong><b>*</b>시군구</strong></label>
							<select title="시/군/구 선택"  name="county1" id="county1" class="add_box" onchange="javascript:changeAreaList(1, this);">
							<option value="">::선택::</option>
							</select>
						</p>
						
						<p class="input">
							<label for="town1_oldaddr"><strong><b>*</b>읍면동</strong></label>
							<select id="town1_oldaddr" class="add_box" name="town1_oldaddr" title="지번주소" onchange="javascript:changeAreaList(1, this); "> 
								<option value="">::선택::</option>
							</select>
						</p>
						
						<p class="input">
							<label for="ri1_oldaddr"><strong><b>*</b>리</strong></label>
							<select id="ri1_oldaddr" class="add_box" name="ri1_oldaddr" title="지번주소"  class="small">  
								<option value="">::선택::</option>
							</select>
							<input type="checkbox" value="" id="mut" name="san"/>
							<label for="mut" class="add_type_1"><strong>산</strong></label>
						</p>
						
						<p class="input">
							<label for="bun1"><strong><b>*</b>번지</strong></label>
							<input type="text" name="bun1" value="" id="bun1" class="add_box" class="text" onkeydown="checkKeyASearch();"/><input type="text" name="bun2" value="" id="bun2" class="add_box" class="text" onkeydown="checkKeyASearch();"/>							
						 </p>
						</div>
						<div class="btn">
							<a href="javascript:normalSearch('1');return false;"><img src="http://www.juso.go.kr/images/link/btn_search.gif" alt="검색" onclick="normalSearch('1');return false;" /></a>
						</div>
					</fieldset>
				</div>
				</form>
  </div>