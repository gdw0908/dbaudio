(function($) {

 var galleryeFfect = function(element, options){
   var settings = $.extend({}, $.fn.galleryeffect.defaults, options); //초반 셋팅값 가져오기
     var vars = {
            currentSlide: 0,
			oldSlide: 0,
			startSlide: 0,
         	totalpage: 0,	
			currentpage: 0,	
			arrawidth:0,
			arraheight:0,
			arrawidth2:0,
            totalSlides: 0,
            randAnim: '',
			titleAnim: '',
            running: false,
            paused: false,
            stop: false
        };

       var slider = $(element);		
	    //이미지사이즈  pg='" + ((vars.totalSlides ) % settings.titcnt) + "'

 	    slider.find('.item').each(function() {
          
			vars.totalSlides++;
		});    
	        
     //초기셋팅 
     

     vars.arrawidth = ((parseInt($(".thumbitem", slider).width()) + 10) * settings.tailCut);
	 vars.totalpage = parseInt($(".item", slider).size()/ settings.tailCut);
	 
	 if( $(".item", slider).size() % settings.tailCut == 0)
		vars.totalpage = vars.totalpage - 1;
	 	
	 if(vars.totalpage > 0){
		 $(".thumb_right", slider).show();
     }
	 else {
	 $(".thumb_right_2", slider).show();
	 }
     $(".thumbnails_body", slider).find("li").eq(vars.currentSlide).addClass("active");
	 $(".thumbitem", slider).eq(vars.currentSlide).addClass("active");
	 var timer = 0;
	 timer = setInterval(function(){ imgeffectRun(slider, settings, false); }, settings.pauseTime);


    var titleshowRun = function(slider, settings, nudge){
if($(".itemdata", slider).eq(vars.currentSlide).find(".cap-title").html() || $(".itemdata", slider).eq(vars.currentSlide).find(".cap-content").html()){
		   if(settings.dataViewType != "none"){
						if(nudge == "in"){
								//내용보기 효과가 랜덤일경우
								if(settings.dataViewType == "Randam"){
									 var anims = new Array('top','bottom','topleft','topright','bottomleft','bottomright'); 
									 vars.titleAnim = anims[Math.floor(Math.random()*(anims.length))];				
									if(vars.titleAnim == undefined) vars.titleAnim = 'top';		
								}else{
								//내용보기효과를 지정한경우
									 vars.titleAnim = settings.dataViewType;	
								}


								if(vars.titleAnim == "top"){
								   $(".itemdata", slider).eq(vars.currentSlide).css({'margin-left':0+'px','margin-top':-80+'px',width:parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',height: 80 + 'px', opacity:'0.6'}).animate({'margin-top':0+'px', opacity:'0.6'}, settings.animSpeed,'',function(){ $(this).find("span").css({opacity:0}).animate({opacity: 1}, settings.animSpeed);});
								}else if(vars.titleAnim == "bottom"){					
									$(".itemdata", slider).eq(vars.currentSlide).css({'margin-left':0+'px','margin-top':parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',height: 80 + 'px',width:parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'0.6'}).animate({height: 80 + 'px', 'margin-top': (parseInt($(".item", slider).eq(vars.currentSlide).height())- 80)+ 'px',opacity:'0.6'}, settings.animSpeed,'',function(){ $(this).find("span").css({opacity:0}).animate({opacity: 1}, settings.animSpeed);});
								}else if(vars.titleAnim == "topleft"){
								   $(".itemdata", slider).eq(vars.currentSlide).css({'margin-left': - parseInt($(".item", slider).eq(vars.currentSlide).width())+'px','margin-top':0+'px',height: 80 + 'px',width:parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'0.6'}).animate({height: 80 + 'px','margin-left':0 +'px', 'margin-top': 0+ 'px',width: parseInt($(".item", slider).eq(vars.currentSlide).width()) +'px', opacity:'0.6'}, settings.animSpeed,'',function(){ $(this).find("span").css({opacity:0}).animate({opacity: 1}, settings.animSpeed);});
								}else if(vars.titleAnim == "topright"){
								   $(".itemdata", slider).eq(vars.currentSlide).css({'margin-left':parseInt($(".item", slider).eq(vars.currentSlide).width())+'px','margin-top':0+'px',width:parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',height: 80 + 'px',opacity:'0.6'}).animate({'margin-left': 0 + 'px', 'margin-top': 0+ 'px', opacity:'0.6'}, settings.animSpeed,'',function(){ $(this).find("span").css({opacity:0}).animate({opacity: 1}, settings.animSpeed);});
								}else if(vars.titleAnim == "bottomleft"){
								  $(".itemdata", slider).eq(vars.currentSlide).css({'margin-left': - parseInt($(".item", slider).eq(vars.currentSlide).width())+'px','margin-top':(parseInt($(".item", slider).eq(vars.currentSlide).height())-80)+'px',height: 80 + 'px',width:parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'0.6'}).animate({height: 80 + 'px','margin-left':0 +'px', width: parseInt($(".item", slider).eq(vars.currentSlide).width()) +'px', opacity:'0.6'}, settings.animSpeed,'',function(){ $(this).find("span").css({opacity:0}).animate({opacity: 1}, settings.animSpeed);});
								}else{
								  $(".itemdata", slider).eq(vars.currentSlide).css({'margin-left':  parseInt($(".item", slider).eq(vars.currentSlide).width())+'px','margin-top':(parseInt($(".item", slider).eq(vars.currentSlide).height())-80)+'px',height: 80 + 'px',width:parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'0.6'}).animate({height: 80 + 'px','margin-left':0 +'px', width: parseInt($(".item", slider).eq(vars.currentSlide).width()) +'px', opacity:'0.6'}, settings.animSpeed,'',function(){ $(this).find("span").css({opacity:0}).animate({opacity: 1}, settings.animSpeed);});

								}
								//$(".itemdata", slider).eq(vars.currentSlide).css({height: 0 + 'px'}).animate({height: 80 + 'px'}, settings.animSpeed,'',function(){ $(this).find("span").css({opacity:0}).animate({opacity: 1}, settings.animSpeed);});
						
						}else if(nudge == "out"){

								if(vars.titleAnim == "top"){
									 $(".itemdata", slider).eq(vars.oldSlide).animate({'margin-top':-80+'px',width:parseInt($(".item", slider).eq(vars.oldSlide).width())+'px',height: 80 + 'px',opacity:0},  settings.animSpeed).find("span").css({opacity:0});
								}else if(vars.titleAnim == "bottom"){
								   $(".itemdata", slider).eq(vars.oldSlide).animate({'margin-top':parseInt($(".item", slider).eq(vars.oldSlide).height())+'px',width:parseInt($(".item", slider).eq(vars.oldSlide).width())+'px',height: 80 + 'px',opacity:0},  settings.animSpeed).find("span").css({opacity:0});
								}else if(vars.titleAnim == "topleft"){
								   $(".itemdata", slider).eq(vars.oldSlide).animate({'margin-left':0+'px','margin-top':0+'px',height: 80 + 'px',width:parseInt($(".item", slider).eq(vars.oldSlide).width())+'px',opacity:0}, settings.animSpeed).find("span").css({opacity:0});
								}else if(vars.titleAnim == "topright"){
									$(".itemdata", slider).eq(vars.oldSlide).animate({'margin-left':0+'px','margin-top':0+'px',height: 80 + 'px',width:parseInt($(".item", slider).eq(vars.oldSlide).width())+'px',opacity:0}, settings.animSpeed).find("span").css({opacity:0});
								}else if(vars.titleAnim == "bottomleft"){
									 $(".itemdata", slider).eq(vars.oldSlide).animate({'margin-left':-parseInt($(".item", slider).eq(vars.oldSlide).width())+'px','margin-top':(parseInt($(".item", slider).eq(vars.oldSlide).height())-80)+'px',height: 80 + 'px',width:parseInt($(".item", slider).eq(vars.oldSlide).width())+'px',opacity:0}, settings.animSpeed).find("span").css({opacity:0});
								}else{
									 $(".itemdata", slider).eq(vars.oldSlide).animate({'margin-left':parseInt($(".item", slider).eq(vars.oldSlide).width())+'px','margin-top':(parseInt($(".item", slider).eq(vars.oldSlide).height())-80)+'px',height: 80 + 'px',width:parseInt($(".item", slider).eq(vars.oldSlide).width())+'px',opacity:0}, settings.animSpeed).find("span").css({opacity:0});
								}



						}
		   }
}
	}    
	//세로방향분활
	   var createHSlices = function(slider, settings, vars){
            for(var i = 0; i < settings.slices; i++){
				var sliceWidth = Math.round($(".item",slider).width()/settings.slices);
              				
				if(i == settings.slices-1){
					
					$(".galleryView",slider).append(
						$('<div class="item-slice"></div>').css({ 
							'margin-left':(sliceWidth*i)+'px', width:($(".item",slider).width()-(sliceWidth*i))+'px',
							height: '0px', 
							opacity:'0', 
							'background-image': 'url("'+ $(".item", slider).eq(vars.currentSlide).attr("img") +'")', 
							'-webkit-background-size':'cover',
							'-moz-background-size':'cover',
							'-o-background-size':'cover',
							'background-size':'cover',
							'background-position': '-'+ ((sliceWidth + (i * sliceWidth)) - sliceWidth) +'px bottom'
						})
					);
				} else {
					
					$(".galleryView",slider).append(
						$('<div class="item-slice"></div>').css({ 
							'margin-left':(sliceWidth*i)+'px', width:sliceWidth+'px',
							height:'0px', 
							opacity:'0', 
							'background-image': 'url("'+ $(".item", slider).eq(vars.currentSlide).attr("img") +'")', 
							'-webkit-background-size':'cover',
							'-moz-background-size':'cover',
							'-o-background-size':'cover',
							'background-size':'cover',
							'background-position': '-'+ ((sliceWidth + (i * sliceWidth)) - sliceWidth) +'px bottom'
						})
					);
				}
				
			}
        }
    //가로방향분활
	   var createWSlices = function(slider, settings, vars){
            for(var i = 0; i < settings.slices; i++){
				var sliceHeight = Math.round($(".item",slider).height()/settings.slices);
              			
				if(i == settings.slices-1){
					
					$(".galleryView",slider).append(
						$('<div class="item-slice"></div>').css({ 
							'margin-top':(sliceHeight*i)+'px', height:($(".item",slider).height()-(sliceHeight*i))+'px',
							width: $(".item",slider).width()+'px', 
							opacity:'1', 
							'background-image': 'url("'+ $(".item", slider).eq(vars.currentSlide).attr("img") +'")', 
							'-webkit-background-size':'cover',
							'-moz-background-size':'cover',
							'-o-background-size':'cover',
							'background-size':'cover',
							'background-position': '0px -'+ ((sliceHeight + (i * sliceHeight)) - sliceHeight) +'px'
						})
					);
				} else {
					
					$(".galleryView",slider).append(
						$('<div class="item-slice"></div>').css({ 
							'margin-top':(sliceHeight*i)+'px', height:sliceHeight+'px',
							width: $(".item",slider).width()+ 'px', 
							opacity:'1', 
							'background-image': 'url("'+ $(".item", slider).eq(vars.currentSlide).attr("img") +'")',  
							'-webkit-background-size':'cover',
							'-moz-background-size':'cover',
							'-o-background-size':'cover',
							'background-size':'cover',
							'background-position': '0px -'+ ((sliceHeight + (i * sliceHeight)) - sliceHeight) +'px'
						})
					);
				}
				
			}
        }


    //박스분활
		var createBoxes = function(slider, settings, vars){
			var boxWidth = Math.round($(".item",slider).width()/settings.boxCols);
			var boxHeight = Math.round($(".item",slider).height()/settings.boxRows);
			
			for(var rows = 0; rows < settings.boxRows; rows++){
				for(var cols = 0; cols < settings.boxCols; cols++){
					if(cols == settings.boxCols-1){
						$(".galleryView",slider).append(
							$('<div class="item-box"></div>').css({ 
								opacity:0,
								left:(boxWidth*cols)+'px', 
								top:(boxHeight*rows)+'px',
								width:(slider.width()-(boxWidth*cols))+'px',
								height:boxHeight+'px',
								'background-image': 'url("'+ $(".item", slider).eq(vars.currentSlide).attr("img") +'")',
							'-webkit-background-size':'cover',
							'-moz-background-size':'cover',
							'-o-background-size':'cover',
							'background-size':'cover'
								,'background-position':' -'+ ((boxWidth + (cols * boxWidth)) - boxWidth) +'px -'+ ((boxHeight + (rows * boxHeight)) - boxHeight) +'px'
							})
						);
					} else {
						$(".galleryView",slider).append(
							$('<div class="item-box"></div>').css({ 
								opacity:0,
								left:(boxWidth*cols)+'px', 
								top:(boxHeight*rows)+'px',
								width:boxWidth+'px',
								height:boxHeight+'px',
								'background-image': 'url("'+ $(".item", slider).eq(vars.currentSlide).attr("img") +'")',
							'-webkit-background-size':'cover',
							'-moz-background-size':'cover',
							'-o-background-size':'cover',
							'background-size':'cover',
								'background-position':' -'+ ((boxWidth + (cols * boxWidth)) - boxWidth) +'px -'+ ((boxHeight + (rows * boxHeight)) - boxHeight) +'px'
							})
						);
					}
				}
			}
		}
	
	var imgViewAnim = function(slider, settings, vars, nudge){
		 if(nudge == "in"){
				 if(settings.ImgViewType == "Randam"){
				 var anims = new Array('sliceDownRight','sliceDownLeft','sliceUpRight','sliceUpLeft','sliceLeft','sliceLeftUp','sliceRight','sliceRightUp','sliceLeftRight','sliceUpDown','sliceUpDownLeft','boxblockRight','fade',
							'boxRandom','boxblock','boxblockDown','boxblockUp','boxblockLeft'); 
						 vars.randAnim = anims[Math.floor(Math.random()*(anims.length))];
				 }else{
					vars.randAnim = settings.ImgViewType;
				 }

				

                if(vars.randAnim == "fade"){
					 $(".item", slider).eq(vars.currentSlide).css({'z-index':20,opacity: 0}).animate({opacity: 1}, settings.animSpeed,'',function(){
						  		 titleshowRun(slider, settings, "in");
					 });
				}else if(vars.randAnim == "sliceDownRight"){
					createHSlices(slider, settings, vars);					
					$(".item-slice", slider).css({ height: parseInt($(".item", slider).eq(vars.currentSlide).height())+'px','margin-top': - parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',opacity:'0'});
                    //오른쪽부터 페이드되면서 내려온다
					var animAddSpeed = 0;
					for(var i=$(".item-slice", slider).size();i >= 0;i--){		
						animAddSpeed = animAddSpeed + 100;
						if(i==0){
							//마지막조각
							   $(".item-slice", slider).eq(i).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20});$(".item-slice", slider).remove();    titleshowRun(slider, settings, "in");		});
						}else{
							   $(".item-slice", slider).eq(i).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ });
						}
					}

				}else if(vars.randAnim == "sliceDownLeft"){
					createHSlices(slider, settings, vars);
					
					$(".item-slice", slider).css({ height: parseInt($(".item", slider).eq(vars.currentSlide).height())+'px','margin-top': - parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',opacity:'0'});
                    //오른쪽부터 페이드되면서 내려온다
					var animAddSpeed = 0;
					for(var i=0;i < $(".item-slice", slider).size();i++){		
						animAddSpeed = animAddSpeed + 100;
						if(i==($(".item-slice", slider).size()-1)){
							//마지막조각
							   $(".item-slice", slider).eq(i).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20});$(".item-slice", slider).remove();    titleshowRun(slider, settings, "in");		});
						}else{
							   $(".item-slice", slider).eq(i).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ });
						}
					}
				}else if(vars.randAnim == "sliceUpRight"){
					createHSlices(slider, settings, vars);
					
					$(".item-slice", slider).css({ height: parseInt($(".item", slider).eq(vars.currentSlide).height())+'px','margin-top': parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',opacity:'0'});
                    //오른쪽부터 페이드되면서 내려온다
					var animAddSpeed = 0;
					for(var i=$(".item-slice", slider).size();i >= 0;i--){		
						animAddSpeed = animAddSpeed + 100;
						if(i==0){
							//마지막조각
							   $(".item-slice", slider).eq(i).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20});$(".item-slice", slider).remove();    titleshowRun(slider, settings, "in");		});
						}else{
							   $(".item-slice", slider).eq(i).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ });
						}
					}
				}else if(vars.randAnim == "sliceUpLeft"){
					createHSlices(slider, settings, vars);
					
					$(".item-slice", slider).css({ height: parseInt($(".item", slider).eq(vars.currentSlide).height())+'px','margin-top': parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',opacity:'0'});
                    //오른쪽부터 페이드되면서 내려온다
					var animAddSpeed = 0;
					for(var i=0;i < $(".item-slice", slider).size();i++){		
						animAddSpeed = animAddSpeed + 100;
						if(i==($(".item-slice", slider).size()-1)){
							//마지막조각
							   $(".item-slice", slider).eq(i).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20});$(".item-slice", slider).remove();    titleshowRun(slider, settings, "in");		});
						}else{
							   $(".item-slice", slider).eq(i).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ });
						}
					}
				}else if(vars.randAnim == "sliceUpDown"){
					createHSlices(slider, settings, vars);					
					$.easing.def = "easeOutElastic";
                    //홀수는 up 짝수는 down
					var animAddSpeed = 0;
				
					for(var i=0;i < $(".item-slice", slider).size();i++){		
						animAddSpeed = animAddSpeed + 100;
						if(i==($(".item-slice", slider).size()-1)){
							//마지막조각
							 if(i%2===0){
							   $(".item-slice", slider).eq(i).css({ height: parseInt($(".item", slider).eq(vars.currentSlide).height())+'px','margin-top': parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',opacity:'0'}).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20});$(".item-slice", slider).remove();   	 titleshowRun(slider, settings, "in");	});
							 }else{
							   $(".item-slice", slider).eq(i).css({ height: parseInt($(".item", slider).eq(vars.currentSlide).height())+'px','margin-top': -parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',opacity:'0'}).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20});$(".item-slice", slider).remove();    titleshowRun(slider, settings, "in");		});
							 }
						}else{
							if(i%2===0){
							   $(".item-slice", slider).eq(i).css({ height: parseInt($(".item", slider).eq(vars.currentSlide).height())+'px','margin-top': parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',opacity:'0'}).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ });
							}else{
						        $(".item-slice", slider).eq(i).css({ height: parseInt($(".item", slider).eq(vars.currentSlide).height())+'px','margin-top': -parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',opacity:'0'}).animate({'margin-top':0+'px',opacity: 1}, (400 + animAddSpeed),'',function(){ });
							}
						}
					}
				}else if(vars.randAnim == "sliceLeft"){
					createWSlices(slider, settings, vars);
                   
					$(".item-slice", slider).css({'margin-left': -parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'0'});
                    var animAddSpeed = 0;
                    for(var i=0;i <$(".item-slice", slider).size();i++){		
						
						animAddSpeed = animAddSpeed + 100;
						if(i==($(".item-slice", slider).size()-1)){
							//마지막조각
							   $(".item-slice", slider).eq(i).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'});$(".item-slice", slider).remove();    titleshowRun(slider, settings, "in");		});
						}else{
							
							   $(".item-slice", slider).eq(i).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ });
						}
					}
				}else if(vars.randAnim == "sliceLeftUp"){
					createWSlices(slider, settings, vars);                   
					$(".item-slice", slider).css({'margin-left': -parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'0'});
                    var animAddSpeed = 0;
                    for(var i=$(".item-slice", slider).size();i >= 0;i--){		
						
						animAddSpeed = animAddSpeed + 100;
						if(i==0){
							//마지막조각
							   $(".item-slice", slider).eq(i).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'});$(".item-slice", slider).remove();   titleshowRun(slider, settings, "in"); 		});
						}else{
							
							   $(".item-slice", slider).eq(i).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ });
						}
					}
				}else if(vars.randAnim == "sliceRight"){
					createWSlices(slider, settings, vars);
                   
					$(".item-slice", slider).css({'margin-left': parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'0'});
                    var animAddSpeed = 0;
                    for(var i=0;i <$(".item-slice", slider).size();i++){		
						
						animAddSpeed = animAddSpeed + 100;
						if(i==($(".item-slice", slider).size()-1)){
							//마지막조각
							   $(".item-slice", slider).eq(i).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'});$(".item-slice", slider).remove();  titleshowRun(slider, settings, "in");  		});
						}else{							
							   $(".item-slice", slider).eq(i).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ });
						}
					}
				}else if(vars.randAnim == "sliceRightUp"){
					createWSlices(slider, settings, vars);
                   
					$(".item-slice", slider).css({'margin-left': parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'0'});
                    var animAddSpeed = 0;
                    for(var i=$(".item-slice", slider).size();i >= 0;i--){		
						
						animAddSpeed = animAddSpeed + 100;
						if(i==0){
							//마지막조각
							   $(".item-slice", slider).eq(i).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'});$(".item-slice", slider).remove();  titleshowRun(slider, settings, "in");  		});
						}else{							
							   $(".item-slice", slider).eq(i).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ });
						}
					}
				}else if(vars.randAnim == "sliceLeftRight"){
					createWSlices(slider, settings, vars);                   
					
                    var animAddSpeed = 0;
					 //홀수는 left 짝수는 right
                    for(var i=0;i <$(".item-slice", slider).size();i++){		
						
						animAddSpeed = animAddSpeed + 100;
						if(i==($(".item-slice", slider).size()-1)){
							//마지막조각
							if(i%2===0){
							   $(".item-slice", slider).eq(i).css({'margin-left': - parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'1'}).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'});$(".item-slice", slider).remove();  titleshowRun(slider, settings, "in");  		});
							}else{
                               $(".item-slice", slider).eq(i).css({'margin-left': parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'1'}).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'});$(".item-slice", slider).remove();   titleshowRun(slider, settings, "in"); 		});
							}
						}else{		
							if(i%2===0){
							   $(".item-slice", slider).eq(i).css({'margin-left': - parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'1'}).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ });
							}else{
							   $(".item-slice", slider).eq(i).css({'margin-left': parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'1'}).animate({'margin-left':'0px', opacity: 1}, (400 + animAddSpeed),'',function(){ });
							}
						}
					}
				}else if(vars.randAnim == "boxRandom"){
                   createBoxes(slider, settings, vars);  
                   var animAddSpeed = 0;
				   $.easing.def = "easeInBounce";
                    for(var i=0;i <$(".item-box", slider).size();i++){		
						
						animAddSpeed = animAddSpeed + 30;
						if(i==($(".item-box", slider).size()-1)){
							//마지막조각
							   $(".item-box", slider).eq(i).animate({opacity: 1}, (100 + animAddSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'});$(".item-box", slider).remove();    titleshowRun(slider, settings, "in");		});
						}else{							
							   $(".item-box", slider).eq(i).animate({opacity: 1}, (100 + animAddSpeed),'',function(){ });
						}
					}
				}else if(vars.randAnim == "boxblock"){
					  createBoxes(slider, settings, vars);  
					   //홀수는 settings.animSpeed 짝수는 settings.animSpeed + settings.animSpeed
					for(var i=0;i <$(".item-box", slider).size();i++){		
												
						if(i==($(".item-box", slider).size()-1)){
							//마지막조각
							if(i%2===0){
							   $(".item-box", slider).eq(i).animate({opacity: 1}, (settings.animSpeed + settings.animSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'});$(".item-box", slider).remove();    titleshowRun(slider, settings, "in");		});
							}else{
							    $(".item-box", slider).eq(i).animate({opacity: 1}, (settings.animSpeed),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'});$(".item-box", slider).remove();  titleshowRun(slider, settings, "in");  		});	
							}
						}else{		
							 if(i%2===0){
							     $(".item-box", slider).eq(i).animate({opacity: 1}, (settings.animSpeed + settings.animSpeed),'',function(){ });
							 }else{
								 $(".item-box", slider).eq(i).animate({opacity: 1}, (settings.animSpeed),'',function(){ });
							 }
						}
					}
				}else if(vars.randAnim == "boxblockDown"){
					  createBoxes(slider, settings, vars);  
					  $.easing.def = "easeOutBounce";
                       $(".item-box", slider).css({'margin-top': - parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',opacity:'0'});
					   var animAddSpeed = 0;
					for(var i=0;i <$(".item-box", slider).size();i++){		
						animAddSpeed = animAddSpeed + 50;					
						if(i==($(".item-box", slider).size()-1)){
							//마지막조각							
							   $(".item-box", slider).eq(i).animate({'margin-top': 0+'px',opacity: 1}, (animAddSpeed + 100),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'}); $(".item-box", slider).remove();   titleshowRun(slider, settings, "in"); 		});
							
						}else{		
							
							 $(".item-box", slider).eq(i).animate({'margin-top': 0+'px',opacity: 1}, (animAddSpeed + 100),'',function(){ });
							
						}
					}
				}else if(vars.randAnim == "boxblockUp"){
					  createBoxes(slider, settings, vars);  
					  $.easing.def = "easeOutBounce";
                       $(".item-box", slider).css({'margin-top':  parseInt($(".item", slider).eq(vars.currentSlide).height())+'px',opacity:'0'});
					   var animAddSpeed = 0;
					for(var i=0;i <$(".item-box", slider).size();i++){		
						animAddSpeed = animAddSpeed + 50;					
						if(i==($(".item-box", slider).size()-1)){
							//마지막조각							
							   $(".item-box", slider).eq(i).animate({'margin-top': 0+'px',opacity: 1}, (animAddSpeed + 100),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'}); $(".item-box", slider).remove();   titleshowRun(slider, settings, "in"); 		});
							
						}else{		
							
							 $(".item-box", slider).eq(i).animate({'margin-top': 0+'px',opacity: 1}, (animAddSpeed + 100),'',function(){ });
							
						}
					}
				}else if(vars.randAnim == "boxblockLeft"){
						  createBoxes(slider, settings, vars);  
						  $.easing.def = "easeInOutBounce";
                       $(".item-box", slider).css({'margin-left': - parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'0'});
					   var animAddSpeed = 0;
					for(var i=0;i <$(".item-box", slider).size();i++){		
						animAddSpeed = animAddSpeed + 50;					
						if(i==($(".item-box", slider).size()-1)){
							//마지막조각							
							   $(".item-box", slider).eq(i).animate({'margin-left': 0+'px',opacity: 1}, (animAddSpeed + 40),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'}); $(".item-box", slider).remove();   titleshowRun(slider, settings, "in"); 		});
							
						}else{		
							
							 $(".item-box", slider).eq(i).animate({'margin-left': 0+'px',opacity: 1}, (animAddSpeed + 40),'',function(){ });
							
						}
					}
				}else{
					createBoxes(slider, settings, vars);  
					$.easing.def = "easeOutElastic";
                       $(".item-box", slider).css({'margin-left': parseInt($(".item", slider).eq(vars.currentSlide).width())+'px',opacity:'0'});
					   var animAddSpeed = 0;
					for(var i=0;i <$(".item-box", slider).size();i++){		
						animAddSpeed = animAddSpeed + 50;					
						if(i==($(".item-box", slider).size()-1)){
							//마지막조각							
							   $(".item-box", slider).eq(i).animate({'margin-left': 0+'px',opacity: 1}, (animAddSpeed + 40),'',function(){ $(".item", slider).eq(vars.currentSlide).css({opacity:1,'z-index':20, 'background-position':'0px 0px'}); $(".item-box", slider).remove();   titleshowRun(slider, settings, "in"); 		});
							
						}else{		
							
							 $(".item-box", slider).eq(i).animate({'margin-left': 0+'px',opacity: 1}, (animAddSpeed + 40),'',function(){ });
							
						}
					}
				}
		 }else if(nudge == "out"){
				 if(vars.randAnim == "fade"){
					 $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "sliceDownRight"){
					  //사라지는거말고 다른거는 숨긴다.
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "sliceDownLeft"){
					 //사라지는거말고 다른거는 숨긴다.
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "sliceUpRight"){
					 //사라지는거말고 다른거는 숨긴다.
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "sliceUpLeft"){
					 //사라지는거말고 다른거는 숨긴다.
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "sliceUpDown"){
					//사라지는거말고 다른거는 숨긴다.
					  $.easing.def = "linear";
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "sliceLeft"){
					//사라지는거말고 다른거는 숨긴다.
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "sliceLeftUp"){
					//사라지는거말고 다른거는 숨긴다.
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "sliceRight"){
					 //사라지는거말고 다른거는 숨긴다.
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
			    }else if(vars.randAnim == "sliceRightUp"){
					 //사라지는거말고 다른거는 숨긴다.
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "sliceLeftRight"){
					 //사라지는거말고 다른거는 숨긴다.
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "boxRandom"){
					 //사라지는거말고 다른거는 숨긴다.
					 $.easing.def = "linear";
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "boxblock"){
					  //사라지는거말고 다른거는 숨긴다.
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "boxblockDown"){
					  //사라지는거말고 다른거는 숨긴다.
					  $.easing.def = "linear";
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "boxblockUp"){
					  //사라지는거말고 다른거는 숨긴다.
					  $.easing.def = "linear";
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else if(vars.randAnim == "boxblockLeft"){
					  //사라지는거말고 다른거는 숨긴다.
					  $.easing.def = "linear";
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}else{
					 //사라지는거말고 다른거는 숨긴다.
					 $.easing.def = "linear";
					  $(".item:not(:eq(" + vars.oldSlide + "))", slider).css({'z-index':1,opacity: 0,'background-position':'0 bottom'});
                      $(".item", slider).eq(vars.oldSlide).css({'z-index':10});
				}


		 }
	}
	var imgeffectRun = function(slider, settings, nudge){
       //Trigger the lastSlide callback
	       
            if(vars && (vars.currentSlide == vars.totalSlides - 1)){ 
				settings.lastSlide.call(this);
			}
            if((!vars || vars.stop) && !nudge) return false;
			settings.beforeChange.call(this);
			//이전꺼 처리
			 
			 $(".thumbnails_body", slider).find("li").eq(vars.oldSlide).removeClass("active");
			 $(".thumbitem", slider).eq(vars.oldSlide).removeClass("active");	
			 titleshowRun(slider, settings, "out");
			 imgViewAnim(slider, settings, vars, "out");		
     
			
			vars.currentSlide++;		
			
			if(vars.currentSlide == vars.totalSlides){ 
				vars.currentSlide = 0;
				vars.startSlide =0;
				
				settings.slideshowEnd.call(this);
			}
    

			
			//현재꺼처리
     
	  //현재수가 7의배수일때
	 if($(".thumbitem", slider).eq(vars.currentSlide).attr("num") =="0"){ vars.currentpage=parseInt($(".thumbitem", slider).eq(vars.currentSlide).attr("pg")); $(".thumbnails_body", slider).find("ul").animate({  left: - (vars.arrawidth * parseInt($(".thumbitem", slider).eq(vars.currentSlide).attr("pg"))) + 'px'}, settings.animSpeed); }   //vars.arrawidth
	 
	 imgViewAnim(slider, settings, vars, "in");	

	 $(".thumbnails_body", slider).find("li").eq(vars.currentSlide).addClass("active");
	 $(".thumbitem", slider).eq(vars.currentSlide).addClass("active");
       
	vars.oldSlide = vars.currentSlide;			
	}
   //함수 호출을 위하여 맨나중에쓴다.
    imgViewAnim(slider, settings, vars, "in");	
    

   //오버설정
    slider.hover(function(){
                vars.paused = true;
                clearInterval(timer);
                timer = '';              
                $(".galleryViewLeft", slider).show();
				$(".galleryViewright", slider).show();
            }, function(){
                vars.paused = false;
				$(".galleryViewLeft", slider).hide();
				$(".galleryViewright", slider).hide();
                //Restart the timer
				if(timer == '' && !settings.manualAdvance){
					timer = setInterval(function(){   imgeffectRun(slider,  settings, false);	}, settings.pauseTime);
				}
      });
      $(".thumbimg", slider).mouseover(function(){
         vars.oldSlide    =  vars.currentSlide;
	     vars.currentSlide = $(this).attr("rel") -1;
	     imgeffectRun(slider,  settings, false);
	  });

	  // prev
	  $(".galleryViewLeft", slider).click(function(){
          vars.currentSlide = vars.currentSlide -2;
		  if(vars.currentSlide == 0){ 
				vars.currentSlide = vars.totalSlides-1;
		  }
          imgeffectRun(slider, settings, false);
	  });
	   $(".galleryViewright", slider).click(function(){
         
          imgeffectRun(slider, settings, false);
	  });
      //left page

	  $(".thumb_left").click(function(){
			$(".thumb_right_2").css("display","none");
	  });

	  $(".thumb_left", slider).click(function(){
            vars.currentpage-- ;
            if(vars.currentpage == 0){ 
				$(".thumb_left", slider).hide();
				$(".thumb_left_2", slider).show();
			}
			$(".thumb_right", slider).show();
			$(".thumbnails_body", slider).find("ul").animate({  left: - (vars.arrawidth * vars.currentpage) + 'px'}, settings.animSpeed); 
			
	  });

	  $(".thumb_right").click(function(){
			$(".thumb_left_2").css("display","none");
	  });

	   $(".thumb_right", slider).click(function(){
            vars.currentpage++ ;
           if(vars.currentpage == vars.totalpage){ 
				$(".thumb_right", slider).hide();
				$(".thumb_right_2", slider).show();
			}
			$(".thumb_left", slider).show();
			$(".thumbnails_body", slider).find("ul").animate({  left: - (vars.arrawidth * vars.currentpage) + 'px'}, settings.animSpeed); 
			
	  });

   settings.afterLoad.call(this);
	return this;
	 };


  
 $.fn.galleryeffect = function(options) {
    //데이터 로딩셋팅
        return this.each(function(key, value){
            var element = $(this);
			
			 galleryeFfect($(element), options);
        });

	};

//Default settings
	$.fn.galleryeffect.defaults = {
		animSpeed: 1000, //이벤트 속도
		pauseTime: 4000, //대기시간
		moveType: "top", //이동방향
		dataViewType: "Randam", //내용보기효과
		ImgViewType:"sliceDownRight",
		tailCut: 0, //썸네일이미지수
		slices:10, //분활수
		boxCols:4, //박스가로분활수
		boxRows:4, //박스세로분활수
		pauseOnHover: true,
		beforeChange: function(){},
		afterChange: function(){},
		slideshowEnd: function(){},
        lastSlide: function(){},
        afterLoad: function(){}
	};
	
	$.fn._reverse = [].reverse;

})(jQuery);
