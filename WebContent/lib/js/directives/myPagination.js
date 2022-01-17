/**
 * 페이지 네비
 * @param ang
 */
(function(ang){
	ang.module('myPagination', []).directive('pagination', [function() {
		return {
			restrict: 'E',
			transclude: true,
			scope:{
				currentPage:'=',
				goPage:'&onSelectPage',
				totalPage : '=',
				blockSize : '=',
			},
			templateUrl : '/lib//js/partials/pagination.html',
			replace : true,
			link : function(scope, elem, attrs){
				var bs = scope.blockSize||10;
				scope.blockPage = 0;
				scope.firstPage = scope.currentPage - ((scope.currentPage-1)%10);
				scope.lastPage = scope.firstPage+bs<=scope.totalPage?scope.firstPage+bs-1:scope.totalPage;
				
				scope.$watch("lastPage", function(newValue, oldValue){
					scope.pages = [];
					for ( var i=scope.firstPage; i<=newValue; i++) {
						scope.pages.push(i);
					}
					if(scope.currentPage > newValue){
						scope.selectPage(newValue);
					}
				});
				scope.$watch("totalPage", function(newValue, oldValue){
					scope.blockPage = Math.ceil((scope.currentPage/bs)-1);
					scope.firstPage = (scope.blockPage*bs)+1;
					scope.lastPage = scope.firstPage+bs<=scope.totalPage?scope.firstPage+bs-1:scope.totalPage;
				});
				scope.isActive = function(page){
					return scope.currentPage == page || page==0;
				};
				scope.selectPage = function(page){
					page = Number(page);
					if(!scope.isActive(page)){
						scope.currentPage = page;
						scope.goPage({page : page});
						
						scope.blockPage = Math.ceil((scope.currentPage/bs)-1);
						scope.firstPage = (scope.blockPage*bs)+1;
						scope.lastPage = scope.firstPage+bs<=scope.totalPage?scope.firstPage+bs-1:scope.totalPage;
					}
				};
				scope.startPage = function() {
	                if (!scope.noPrevious()) {
	                    scope.selectPage(1);
	                }
	            };
				scope.endPage = function() {
	                if (!scope.noNext()) {
	                    scope.selectPage(scope.totalPage);
	                }
	            };
				scope.prevPage = function() {
	                if (!scope.noPrevious()) {
	                    scope.selectPage((scope.lastPage - 10) < 1 ? 1 : (scope.firstPage - 10));
	                }
	            };
				scope.nextPage = function(){
					if (!scope.noNext()) {
	                    scope.selectPage((scope.firstPage + 10) > scope.totalPage ? scope.totalPage : (scope.firstPage + 10));
	                }
				};
				scope.noPrevious = function() {
	                return scope.currentPage == 1;
	            };
				scope.noNext = function() {
	                return scope.currentPage == scope.totalPage;
	            };
				scope.onActiveStyle = function(page) {
	                return scope.isActive(page)?{color: '#2c7bca', 'font-weight':'bold'}:"";
	            };
			}
		};
	}]);
})(angular);