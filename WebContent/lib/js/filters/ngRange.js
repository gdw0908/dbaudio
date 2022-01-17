/**
 * 숫자증가 배열 필터
 * @param ang
 * @param $
 */
(function(ang, $){
	var DEBUG = true;
	ang.module('ngRange', []).filter('range', function() {
        return function(input, len) {
            var lowBound, highBound;
            switch (input.length) {
            case 1:
                lowBound = 0;
                highBound = parseInt(input[0]) - 1;
                break;
            case 2:
                lowBound = parseInt(input[0]);
                highBound = parseInt(input[1]);
                break;
            default:
                return input;
            }
            var result = [];
            for (var i = lowBound; i <= highBound; i++){
            	if(!!len){
                	i= '000000000000000'+i; 
                    result.push(i.substring(i.length-len));
            	}else{
                    result.push(i);
            	}
            }
            return result;
        };
    });
	
})(angular, jQuery=(typeof window.jQuery=='undefined'?(function(){alert("jQuery 를 import 하세요."); return "";})():jQuery));