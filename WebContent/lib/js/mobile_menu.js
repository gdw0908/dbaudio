
//모바?? ?�라?�드 메뉴 - ?�브메뉴 ?�코?�언
$.fn.accordJs = function(){
	$('.m_menu_parent',this).each(function(index){
		$(this).click(function(){
			var visb = $(this).next().css('display');
			if(visb =='none'){
				$(this).removeClass('open');
				$(this).addClass('opened');
			}else{
				$(this).removeClass('opened');
				$(this).addClass('open');
			}
			$('.mbm_sub_lst:eq('+index+')').slideToggle();
		});
	});
};

//모바?? ?�라?�드 메뉴 - ?�라?�드 모션
var opens = false;
$(".my_menu a").click(function(){
	if(opens == false){
		$("html").css({"overflow-x":"hidden"});
		$("body").css({"overflow":"hidden"});
		$(".hide_menu").css({"display":"block", "box-shadow":"3px 3px 25px #3c3939"}).animate({"left":"0"}, 200,function(){
			//$('.hide_menu .toHome a').focus();
		});
		$(".wrap").css({"position":"relative","height":"100%"});
		$(".left_back").css({"display":"block"});
		$(".sl_close a").css({"display":"block"});
		opens = true;
	}else{
		$("html").css({"overflow-x":"visible"});
		$("body").css({"overflow":"visible"});
		$(".hide_menu").animate({"left":"-85%"}, 200).css({"height":"100%", "box-shadow":"none"});
		$(".wrap").css({"position":"static"});
		$(".left_back").css({"display":"none"});
		opens = false;
	}
});

$(".sl_close a").click(function(){
		$("html").css({"overflow-x":"visible"});
		$("body").css({"overflow":"visible"});
		$(".hide_menu").animate({"left":"-85%"}, 200).css({"height":"100%", "box-shadow":"none"});
		$(".wrap").css({"position":"static"});
		$(".left_back").css({"display":"none"});
		$(this).css({"display":"none"});
		opens = false;
});

$('.accord').accordJs();