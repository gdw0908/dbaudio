$(document).ready(function(){
	
    //gnb menu
    var $gnb = $("#gnbmenu .inner > ul > li"); 
    var $gnbDepth2 = $("#gnbmenu .depthLayer");
    $(".brandOverAni li a").hover(function(){
        brandOverAni(this);
    },function(){
        brandOverAniOut(this);
    });
    $gnb.mouseenter(function(){        
		var $gnbItemW = $(this).width() / 2;
		var $offL = $(this).position().left;
		var $offtotal = $offL + $gnbItemW - 5;
		$("#gnbmenu .arrow").show().stop(true, false).animate({left:$offtotal}, 0, 'easeOutCubic');
		
		var target = $(this);
		
		$gnbDepth2.css('display','none');
		target.find(".depthLayer").css('display','block');
		$gnb.removeClass('active');
		target.addClass("active");
		
		if( target.hasClass('g4') || target.hasClass('g5') || target.hasClass('g6') || target.hasClass('g7') || target.hasClass('g8') || target.hasClass('g9')){
			$gnbDepth2.removeClass("active").height(354);//.stop().animate({height:'360px'},0,'easeOutExpo');    
		} 
		else if(target.hasClass('g10') || target.hasClass('g11') || target.hasClass('g12')) {
			$gnbDepth2.removeClass("active").height(354);//.stop().animate({height:'470px'},0,'easeOutExpo');
		} 
		else {
			$gnbDepth2.removeClass("active").height(354);//.stop().animate({height:'390px'},0,'easeOutExpo');
		}
		
		target.find(".depthLayer").addClass("active");

		if (!target.hasClass("cycle_init") ) {
			
			target.addClass("cycle_init");
			
			if (target.find(".jquery_cycle_wrap").length > 0 ) {
				initJqueryCycle(target.find(".jquery_cycle_wrap"), {
					cycle_opt : {
						"slides" : "> .div_cycle"
						, "continueAuto" : target.find(".jquery_cycle_wrap").attr("id") == "requirItemEvent" ? true : false
						, "fx" : "scrollHorz"
						, "random" : true
					}
					, site_opt : {
						isCycle : false
						, isImgSrcChg : true
					}
				});
			}
		}
    });
    
    $gnb.mouseleave(function(){
        $("#gnbmenu .arrow").hide();
        $gnb.removeClass("active");
        $gnbDepth2.removeClass("active").css({height:'0'});
        $.each($("#gnbmenu .inner > ul > li"),function(){
			if(this.id == 'on_gmenu'){
				$(this).addClass("active");
				$(this).find(".depthLayer").addClass("active").css('display','block').height("auto");
			}
		});
    });

    //작은네비에서 gnb 오버시
    $(".headerInner").hover(function(){
        if($('.headerInner').hasClass('fix')){
            $("#gnbmenu").stop().animate({ top:'58px' },0,'easeOutCubic');    
        }
    },function(){
        $("#gnbmenu").stop().animate({ top:'0'},0,'easeOutCubic');
    });

	 
});
