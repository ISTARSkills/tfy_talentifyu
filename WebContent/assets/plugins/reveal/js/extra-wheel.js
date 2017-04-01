/*
 * extra-wheel.js
 * 
 * Contains all the handlers used in play_session.jsp for reveal to work!
 * Touch only with extreme confidence and cause
 * 
 */


	function handleToggleSessionListJump(horizontalSlideIndex) {
		//console.log("handleToggleSessionListJump('" + horizontalSlideIndex + "');");
		
		if(horizontalSlideIndex != 0){
			$("#session_list_button").text('X').css('background-color', '#131313').css('color', '#08e3ff');
			if(horizontalSlideIndex!=0) {
				$("#session_list_button").data("slidenumber" , horizontalSlideIndex);
			}
 			Reveal.slide(0);
			updateSlideBgColor();
		} else {
			$("#session_list_button").text('i').css('background-color', '#08e3ff').css('color', '#131313');
		    var slide_number = $("#session_list_button").data("slidenumber");
	 		Reveal.slide(slide_number);
			updateSlideBgColor();
		}
		
		return ;
	}

	function restoreHistory(slide_id) {
		//console.log("restoreHistory('" + slide_id + "');");
		
		if(slide_id > 0) {
		    var slide_number = 0;
			var temp = -1;
			
			$( ".slide" ).each(function( index ) {
				  if($( this ).attr("id") == slide_id) {
					  slide_number = temp;
				  } else {
					  temp = temp + 1;
				  }
			});
			
			if(slide_number > 0) {
				Reveal.slide(slide_number,0, 0);
				updateSlideBgColor();
				
			}
		} else {
			Reveal.slide(1);
			updateSlideBgColor();
		}
	}

	function handlePlaybackButton(event) {
		//console.log("handlePlaybackButton('" + event + "');");
		
		switch(event) {
		case "ready" :
			break ;
		case "autoslidepaused" :
			break ;
		case "autoslideresumed" :
			break ;
		case "session_list_button" :
			if(Reveal.getIndices().h==0) { diablePlaybackButton(); } 
			else { enablePlaybackButton(); }
			break ;
		default :
			break;
		}
	
		return ;
	}
	
	function handleVideoFrameSize(event) {
		//console.log("handleVideoFrameSize('" + event + "');");
		switch(event) {
		case "ready" :
			$( ".ONLY_VIDEO" ).each(function( index ) {
	 			var slides_scale = $('.slides').css('transform').split(',')[3];
	 			var offset = $(".slides").offset();
	 			var posY = offset.top - $(window).scrollTop();
	 			var posX = offset.left - $(window).scrollLeft();
	 			
	 			var vid_height = $( window ).height()/slides_scale;
	 			var vid_width = $( window ).width()/slides_scale;
				var vid_top = posY/slides_scale;
				var vid_left = posX/slides_scale;
				$("video."+ $( this ).attr("id")).css("height",vid_height).css("width",vid_width).css("margin-top",-vid_top).css("margin-left",-vid_left);
			});
			break ;
		default :
			break;
		}
	
		return ;
	}
	
	function handleTouch(event) {
		//console.log("handleTouch('" + event + "');");
		
		switch(event) {
		case "ready" :
			break ;
		case "autoslidepaused" :
			enableSwipe();
			break ;
		case "autoslideresumed" :
			diableSwipe();
			break ;
		case "session_list_button" :
			if(Reveal.getIndices().h==0){ diableSwipe(); } 
			else { enableSwipe(); }
			break ;
		default :
			break;
		}
	
		return ;
	}
	
	function handleMedia(event) {
		//console.log("handleMedia('" + event + "');");
		
		switch(event) {
		case "ready" :
			handleAudio("play");
			handleVideo("play");
			break ;
		case "slidechanged" :
			var isOnAutoSlide = !!Reveal.isAutoSliding();
			if(($('#cover-div').width() > 0 ) && (isOnAutoSlide)) {
				handleAudio("play");
				handleVideo("play");
			}
			break ;
		case "autoslidepaused" :
			handleAudio("pause");
			handleVideo("pause");
			break ;
		case "autoslideresumed" :
			handleAudio("play");
			handleVideo("play");
			break ;
		case "session_list_button" :
			break ;
		default :
			break;
		}
		
		return ;
	}
	
	function handleAudio(action) {
		if($('#presentation_audio').length) {
			switch (action) {
			case "play" :
				if($('#presentation_audio')[0].currentTime == 0) {
					$('#presentation_audio')[0].play();
				} else if($('#presentation_audio')[0].paused == true){
					var elapsedTime = 0;
					$('section.past').each(function(index) {
						if($(this).data("autoslide")!=null) {
							elapsedTime = elapsedTime + $(this).data("autoslide");
							console.log("dT -> " + $(this).data("autoslide"));
							console.log("eT0 -> " + elapsedTime);
						}
					});
					$('#presentation_audio')[0].currentTime = elapsedTime/1000;
					$('#presentation_audio')[0].play();
				}
				break;
			case "pause" :
				$('#presentation_audio')[0].pause();
				break;
			default :
				break;	
			}
		} else if($("section.present > audio").length){
			switch (action) {
			case "play" :
				$("section.present > audio")[0].currentTime = 0;
				$("section.present > audio")[0].play();
				break;
			case "pause" :
				$("section.present > audio")[0].pause();
				break;
			default :
				break;	
			}
		}
	}
	
	function handleVideo(action) {
		if($("section.present > video").length){
			switch (action) {
			case "play" :
				$("section.present > video")[0].currentTime = 0;
				$("section.present > video")[0].play();
				break;
			case "pause" :
				$("section.present > video")[0].pause();
				break;
			default :
				break;	
			}
		}
	}
	
	function handleNavigationButtons(event) {
		//console.log("handleNavigationButtons('" + event + "');");
		
		if(Reveal.getIndices().h==0) {
			$('#left-nav-div').addClass("hideDiv");
			$('#right-nav-div').addClass("hideDiv");
		} else if(Reveal.isLastSlide()) {
			$('#right-nav-div').addClass("hideDiv");
		} else if(Reveal.getIndices().h==1) {
			$('#left-nav-div').addClass("hideDiv");
			$('#right-nav-div').removeClass("hideDiv");
		} else {
			$('#left-nav-div').removeClass("hideDiv");
			$('#right-nav-div').removeClass("hideDiv");
		}
	}
	
	function resetCurrentSlideFragments(event) {
		//console.log("resetCurrentSlideFragments('" + event + "');");
		
		($('.slide.present'))[0].querySelectorAll( '.fragment' ).forEach( function( mediaElement ) {
			Reveal.navigateFragment(null, -1) ;
		});
	}
	
	function handleAutoSlideShow(event) {
		//console.log("handleAutoSlideShow('" + event + "');");
		
		var isOnAutoSlide = !!Reveal.isAutoSliding();
		if($('.present').attr('id')!=0 && isOnAutoSlide) {
			Reveal.onAutoSlidePlayerClick();
		} else if ($('.present').attr('id')==0 && !isOnAutoSlide) {
			Reveal.onAutoSlidePlayerClick();
		}
	}
	
	function updateURL(event) {
		/* var currentURL = window.location.href;  var res = currentURL.split("#"); currentURL = res[0] ; */
		history.pushState({}, "URL Rewrite Example", window.location.href.split("#")[0] + "#" + event.currentSlide.id);
	}
	
	function updateSlideBgColor() {
		try {
			if ($('.present').data("bgcolor") == "none") { document.body.style.background = orgBgColor; } 
			else { document.body.style.background = $('.present').data("bgcolor"); }
		} catch(err) { }
	}
	
	function handleButtonVisibility(event) {
		//console.log("handleButtonVisibility('" + event + "');");

		switch(event) {
		case "autoslideresumed" :
			$("#left-nav-div").fadeOut(1000);
			$("#right-nav-div").fadeOut(1000);
			$("#session_list_button").fadeOut(1000);
			break ;
		case "autoslidepaused" :
			$("#left-nav-div").fadeIn(1000);
			$("#right-nav-div").fadeIn(1000);
			$("#session_list_button").fadeIn(1000);
			break ;
		case "ready" :
			$("#left-nav-div").fadeOut(1000);
			$("#right-nav-div").fadeOut(1000);
			$("#session_list_button").fadeOut(1000);
			break ;
		default :
			break;
		}
	}
	
	function handleFragmentVisibility(event) {
		//console.log("handleFragmentVisibility('" + event + "');");
		
		switch(event) {
		case "fragmentshown" :
			if ($(event.fragment).attr('id') == "737373") {
				$('.present').find('.fragment').each(function(index, value) { $(this).addClass("current-fragment show-all"); });
				$(event.fragment).removeClass("current-fragment");
			}
			break ;
		case "slidechanged" :
			$('.present').find('.show-all').each(function(index, value) { $(this).removeClass("show-all"); });
			break ;
		default :
			break;
		}
		
		return ;
	}

	function toggleSwipe() {
		if($("#cover-div").width()==0) {
			diableSwipe();
		} else {
			enableSwipe();
		}
	}
	
	function enableSwipe() {
		$("#cover-div").width("0%");
	}
	
	function diableSwipe() {
		$("#cover-div").width("100%");
	}
	
	function diablePlaybackButton() {
		$(".playback").fadeOut(500);
	}
	
	function enablePlaybackButton() {
		$(".playback").fadeIn(500);
	}