/**
 * 유틸 디렉티브
 * @param ang
 */
(function(ang){
	ang.module('myUtil', [])
	.directive('onlyNumber', function () {
	    return {
	        require: 'ngModel',
	        link: function (scope, elem, attrs, ngModelCtrl) {
	        	ngModelCtrl.$parsers.push(function (inputValue) {
	        		if (inputValue == undefined) return '' 
	                var transformedInput = inputValue.replace(/[^0-9+.]/g, ''); 
	                if (transformedInput!=inputValue) {
	                	ngModelCtrl.$setViewValue(transformedInput);
	                	ngModelCtrl.$render();
	                }         
	                return transformedInput;  
	            });
	        }
	    };
	})
	.directive('myHref', function($location) {
		return {
	        link: function(scope, elem, attrs) {
	        	elem.on('click', function() {
	                scope.$apply(function() {
	                    $location.path(attrs.myHref);
	                });
	            });
	        }
	    }
	})
	.filter('num', function() {
		return function(input) {
	        return Number(input);
		}
	})
	//렌더링 끝나면 실행하는 함수
	.directive('onFinishRender', function ($timeout) {
		return {
	        restrict: 'A',
	        link: function (scope, element, attr) {
	            if (scope.$last === true) {
	            	$timeout(function() { 
	            		scope.$eval(attr.onFinishRender);
	            	}, 100);
	            }
	        }
    	}
	})
	.directive('pwCheck', [function () {
	    return {
	        require: 'ngModel',
	        link: function (scope, elem, attrs, ctrl) {
	            var firstPassword = '#' + attrs.pwCheck;
	            elem.add(firstPassword).on('keyup', function () {
	                scope.$apply(function () {
	                    ctrl.$setValidity('pwmatch', elem.val() === $(firstPassword).val());
	                });
	            });
	        }
	    }
	}]).directive('ngChecklist', function() {
		  return {
			    scope : {
				all : '=ngCheckall',
				list : '=ngChecklist',
				value : '@'
			},
			link : function(scope, elem, attrs) {
				$(elem).on('change', function() {
					scope.$apply(function() {
						var checked = $(elem).prop('checked');
						var index = $.inArray(scope.value, scope.list);

						if (checked && index == -1) {
							scope.list.push(scope.value);
						} else if (!checked && index != -1) {
							scope.list.splice(index, 1);
						}
					});
				});

				scope.$watch('list', function() {
					var checked = $(elem).prop('checked');
					var index = $.inArray(scope.value, scope.list);

					if (checked && index == -1) {
						$(elem).prop('checked', false);
					} else if (!checked && index != -1) {
						$(elem).prop('checked', true);
					}
				}, true);
				
				scope.$watch('all', function(newValue, oldValue) {
					if(angular.equals(newValue, oldValue)){
					    return;
					}
					if(newValue){
						for ( var i in scope.list) {
							var item = scope.list[i];
							$(item).prop('checked', true);
						}
					}else{
						for ( var i in scope.list) {
							var item = scope.list[i];
							$(item).prop('checked', false);
						}
					}
				}, true);
			}
		};
	}).directive('bindHtmlUnsafe', function( $compile ) {
	    return function( $scope, $element, $attrs ) {

	        var compile = function( newHTML ) { // Create re-useable compile function
	            newHTML = $compile(newHTML)($scope); // Compile html
	            $element.html('').append(newHTML); // Clear and append it
	        };

	        var htmlName = $attrs.bindHtmlUnsafe; // Get the name of the variable 
	                                              // Where the HTML is stored

	        $scope.$watch(htmlName, function( newHTML ) { // Watch for changes to 
	                                                      // the HTML
	            if(!newHTML) return;
	            compile(newHTML);   // Compile it
	        });

	    };
	}).directive('datepicker', function () {
		return {
	        restrict: 'A',
	        require : 'ngModel',
	        link : function (scope, element, attrs, ngModelCtrl) {
	            $(function(){
	                element.datepicker({
		                changeYear: true,
	        			changeMonth: true,
	        			showMonthAfterYear: true,
	        			monthNames : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'], 
	        			monthNamesShort : ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'], 
	        			dayNames : ['일', '월', '화', '수', '목', '금', '토'],
	        			dayNamesShort : ['일', '월', '화', '수', '목', '금', '토'],
	        			dayNamesMin : ['일', '월', '화', '수', '목', '금', '토'],
	        			dateFormat:'yy-mm-dd',
	                    showOn: "button",      
	            		buttonImage: "/images/admin/contents/calendar.png",
	                    onSelect:function (date) {
	                        scope.$apply(function () {
	                            ngModelCtrl.$setViewValue(date);
	                        });
	                    }
	                });
	            });
	        }
	    }
	}).directive('errSrc', function() {
		return {
		    link: function(scope, element, attrs) {
		    	element.bind('error', function() {
			        if (attrs.src != attrs.errSrc) {
			        	attrs.$set('src', attrs.errSrc);
			        }
		    	});
		    }
		}
	});
})(angular);