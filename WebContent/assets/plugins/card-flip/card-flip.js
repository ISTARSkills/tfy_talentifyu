
		$( ".card-flipper" ).bind( "click", function() {
			cardId = $(this).attr('id');
		    $("#qCard-"+cardId).toggleClass('flipped');
		});

		$( ".swiper-button-next, .swiper-button-prev" ).bind( "click", function() {
			 resetFlipStates() ;
		});
		
		function resetFlipStates() {
			document.querySelectorAll( '.card.flipped' ).forEach( function( element ) {
				$(element).toggleClass('flipped');
			} );
		}