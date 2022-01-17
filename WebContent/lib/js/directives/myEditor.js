(function(ang){
	ang.module('myEditor', []).directive('smarteditor', ['$interval', function($interval) {
		return {
	        restrict: 'A',
			transclude: true,
	        require: 'ngModel',
	        replace : false,
	        link : function(scope, elem, attrs, ctrl){
	        	
	        	var oEditors = [];
	        	nhn.husky.EZCreator.createInIFrame({
	        		oAppRef: oEditors,
	        		elPlaceHolder: attrs.id,
	        		sSkinURI: "/smarteditor/SmartEditor2Skin.html",	
	        		htParams : {
	        			bUseToolbar : true,				// 툴바 사용 여부 (true:사용/ false:사용하지 않음)
	        			bUseVerticalResizer : true,		// 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
	        			bUseModeChanger : true,			// 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
	        			//aAdditionalFontList : aAdditionalFontSet,		// 추가 글꼴 목록
	        			fOnBeforeUnload : function(){
	        				//alert("완료!");
	        			}
	        		}, //boolean
	        		fOnAppLoad : function(){
	        			var stop = $interval(function(){
	        				try{
				        		if(oEditors && oEditors.getById[attrs.id].getEditingAreaHeight()>0){
				        			if(ctrl.$viewValue != oEditors.getById[attrs.id].getIR()){
					        			ctrl.$setViewValue(oEditors.getById[attrs.id].getIR());
				        			}
				        			
				        		}else{
				        			$interval.cancel(stop);
				        		}
			        		}catch(e){
		        				$interval.cancel(stop);
		        			}
			        	},1000);
	        			//예제 코드
	        			//oEditors.getById["ir1"].exec("PASTE_HTML", ["로딩이 완료된 후에 본문에 삽입되는 text입니다."]);
	        		},
	        		fCreator: "createSEditor2"
	        	});
	        	
	        }
		}
	}]);
})(angular);