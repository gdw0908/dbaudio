$(function() {
	// 햄버거 메뉴
	$(document).ready(function(){
		$('.ham_wrap').click(function(){
			$(this).toggleClass('open');
			$('.mo_menu_wrap').toggleClass('active');
			$('.mo_bg').toggleClass('active');
			$('body').toggleClass('active');
		});
	});
	
	//전체 카테고리
	$("#allmenu_open").click(function() {
		$("#all_menu").slideToggle(250);
		$('i.xi-bars').toggleClass('xi-close');
	});
	$("#allmenu_close").click(function() {
		$("#all_menu").slideToggle(250);
	});

});
