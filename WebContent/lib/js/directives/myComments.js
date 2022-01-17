/**
 * 댓글
 * @param ang
 */
(function(ang){
	ang.module('myComments', ['myCommon']).directive('comments', [function() {
		return {
	        restrict: 'A',
			templateUrl : function(elem, attrs){
				// 1 : 사용자, else 관리자
				if(attrs.ngMode == 1){
					return '/lib/js/partials/comments.html';
				}else{
					return '/lib/js/partials/comments.html';
				}
			},
	        scope: {
	        	ngMode : '=',
	        	articleSeq: '='
	        },
	        replace : true,
	        controller : function($scope, $compile, ajaxService){
	        	$scope.form1 = {
	        			type : ($scope.ngMode)?$scope.ngMode.toString():"",
	        			article_seq : $scope.articleSeq
	        	};
	        	$scope.form2 = {
	        			type : ($scope.ngMode)?$scope.ngMode.toString():"",
	        			article_seq : $scope.articleSeq
	        	};
	        	
	        	var copyForm1 = ang.copy($scope.form1);
	        	var copyForm2 = ang.copy($scope.form2);
	        	
	        	$scope.init = function(){
					$scope.form1 = ang.copy(copyForm1);
					$scope.form2 = ang.copy(copyForm2);
	        		ajaxService.getJSON('/json/list/article.comment.do', {article_seq : $scope.articleSeq}, function(data){
	        			$scope.comments = data;
	        		});
	        	}
	        	$scope.init();
	        	
				$scope.reply = function(item, obj){
					$scope.form1.parent_seq = item.seq;
					$("#reply_ready").remove();

		        	var commentHtml = "";
	        		commentHtml += '<li class="reply" id="reply_ready">';
	        		commentHtml += '	<form name="commentFrm1">';
	        		commentHtml += '	<div class="area_box"><p class="user">@'+item.reg_nm + '</p>';
	        		commentHtml += '		<textarea class="textarea_1" data-ng-model="form1.conts" required="required"></textarea>';
	        		commentHtml += '		<a class="comm_btn type_2" data-ng-click="save1()"><img src="/images/article/btn_comment.gif" alt="덧글달기" /></a>';
	        		commentHtml += '	</div>';
	        		commentHtml += '	</form>';
	        		commentHtml += '</li>';
	        		$(obj.target).closest("li").after($compile(commentHtml)($scope));
				}
				
				
				$scope.save1 = function(){
					if($scope.commentFrm1.$invalid){
						alert("값이 유효하지 않습니다.");
						$(".ng-invalid")[1].focus();
						return false;
					}
					$scope.save($scope.form1);
				}
				
				$scope.save2 = function(){
					if($scope.commentFrm2.$invalid){
						alert("값이 유효하지 않습니다.");
						$(".ng-invalid")[1].focus();
						return false;
					}
					$scope.form2.parent_seq = 0;
					$scope.save($scope.form2);
				}
				
				$scope.save = function(form){
					return ajaxService.getJSON('/article.do?mode=commentSave', {jData : JSON.stringify(form)}, function(data){
						if(data.rst == "-1"){
							alert(data.msg);
						}else{
							$scope.init();
						}
					});
				}
				
				$scope.del = function(item){
					var param = {
						type : $scope.ngMode,
						seq : item.seq
					};
					if(!confirm("정말 삭제하시겠습니까?")){
						return false;
					}else{
						ajaxService.getJSON('/article.do?mode=commentDel', param, function(data){
							if(data.rst == '1'){
								$scope.init();
							}else{
								alert(data.msg);
							}
						});
					}
				}
				
				$scope.cancel = function(){
					$("#reply_ready").remove();
				}
				
	        }
	    };
	}]);
})(angular);