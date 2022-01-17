//on off

$(document).ready(function() {

	$(".btn").mouseover(function() {
		var file = $(this).attr('src').split('/');
		var filename = file[file.length - 1];
		var path = '';
		for (i = 0; i < file.length - 1; i++) {
			path = (i == 0) ? path + file[i] : path + '/' + file[i];
		}
		$(this).attr('src', path + '/' + filename.replace('_off', '_on'));

	}).mouseout(function() {
		var file = $(this).attr('src').split('/');
		var filename = file[file.length - 1];
		var path = '';
		for (i = 0; i < file.length - 1; i++) {
			path = (i == 0) ? path + file[i] : path + '/' + file[i];
		}
		$(this).attr('src', path + '/' + filename.replace('_on', '_off'));
	});

});

//장바구니 추가
function addCart(item_seq){

   if(confirm("선택한 제품을 장바구니에 추가하시겠습니까?")){
      var seq =  $("input[name='item_seq']").val(item_seq);
      
      $.getJSON("/giftcard/mypage/shopping/cart/index.do?mode=add_cartAjax", {
			seq : item_seq,
			qty : '1'
      }, function(data) {
    	  
    	  if (data.rst == "1") {
				if(confirm("장바구니로 이동하시겠습니까?")){
					location.href = "/giftcard/mypage/shopping/cart/index.do";
					alert("장바구니에 추가되었습니다.");
				} else {
					location.reload();					//새로고침
					return alert("장바구니에 추가되었습니다.");
					
				}
			} else {
				alert("장바구니 추가 오류입니다.");		
			}
		});     
   } else {
	   return;
   }
}