/**
 * 마이 필터 모음
 * @param ang
 * @param $
 */
(function(ang, $){
	angular.module('myFilter', [])
	.filter('inArray', function() {
	    return function(input, param1, param2) {
	    	var result = [];
	    	for ( var i in input) {
	    		var map = $.map(param1, function(item){
	    				return item[param2]; 
	    			});
	    		if($.inArray(input[i].member_seq, map) < 0){
	    			result.push(input[i]);
	    		}
			}    	
	        return result;
	    };
	})
	//오라클 날짜 그냥 사용하기 위한 필터
	.filter('myDate', function($filter) {
	    var angularDateFilter = $filter('date');
	    return function(theDate, format) {
	    	if(!theDate){
	    		return "";
	    	}
	    	try{
	    		theDate = theDate.replace(/-/g,'/').replace(/\.0$/, '');
	    	}catch(e){}
	    	var dt = new Date(theDate);
	    	if(isNaN(dt.getTime())){
	    		return "날짜형식오류";
	    	}else{
	    		if(format){
	    			return angularDateFilter(dt, format);
	    		}else{
	    			return angularDateFilter(dt, 'yyyy/MM/dd HH:mm:ss');
	    		} 
	    	}
	    }
	});
	
})(angular, jQuery=(typeof window.jQuery=='undefined'?(function(){alert("jQuery 를 import 하세요."); return "";})():jQuery));