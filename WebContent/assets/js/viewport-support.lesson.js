/*
 * CSS viewport units with jQuery
 * http://www.w3.org/TR/css3-values/#viewport-relative-lengths
 */


//Usage demo:
/*
$('div').css({
  height: '50vh',
  width: '50vw',
  marginTop: '25vh',
  marginLeft: '25vw',
  fontSize: '10vw'
});*/

/****************************************************************** DO NOT TOUCH ******************************************************************************/

;(function( $, window ){

  var $win = $(window)
    , _css = $.fn.css;

  function viewportToPixel( val ) {
    var percent = val.match(/[\d.]+/)[0] / 100
      , unit = val.match(/[vwh]+/)[0];
    return (unit == 'vh' ? $win.height() : $win.width()) * percent +'px';
  }

  function parseProps( props ) {
    var p, prop;
    for ( p in props ) {
      prop = props[ p ];
      if ( /[vwh]$/.test( prop ) ) {
        props[ p ] = viewportToPixel( prop );
      }
    }
    return props;
  }

  $.fn.css = function( props ) {
    var self = this
      , originalArguments = arguments
      , update = function() {
          if ( typeof props === 'string' || props instanceof String ) {
            if (originalArguments.length > 1) {
              var argumentsObject = {};
              argumentsObject[originalArguments[0]] = originalArguments[1];
              return _css.call(self, parseProps($.extend({}, argumentsObject)));
            } else {
              return _css.call( self, props );
            }
          } else {
            return _css.call( self, parseProps( $.extend( {}, props ) ) );
          }
        };
    $win.resize( update ).resize();
    return update();
  };

}( jQuery, window ));

/****************************************************************** DO NOT TOUCH ******************************************************************************/


/* end_of_assessment.jsp */
$('.end-of-assessment').css({
	"height": "100vh",
	"width": "100vw"
});


/* already_attempted.jsp */
$('.already-attempted').css({
	"height": "100vh",
	"width": "100vw"
});


/* end_of_course.jsp */
$('.end-of-course').css({
	"height": "100vh",
	"width": "100vw"
});


/* end_of_session.jsp */
$('.end-of-session').css({
	"height": "100vh",
	"width": "100vw"
});


/* yellow.jsp */
$('.launch-assessment-page .session-title').css({
	"font-size": "10vw"
});

$('.launch-assessment-page .lesson-title').css({
	"font-size": "8vw"
});

$('.launch-assessment-page .instructions').css({
	"font-size": "4.5vw"
});

$('.assessment-page, .launch-assessment-page').css({
	"min-height": "97vh"
});

$('#left-nav-div').css({
	"padding-top": "50vh",
	"height": "75vh"
});

$('#right-nav-div').css({
	"padding-top": "50vh",
	"height": "90vh"
});

$('.reveal .ONLY_TITLE_PARAGRAPH_IMAGE  table').css({
	"font-size": "4vh"
});

$('.reveal .ONLY_PARAGRAPH  table').css({
	"font-size": "4vh"
});

$('.reveal .ONLY_TITLE_PARAGRAPH_cells_fragemented  table').css({
	"font-size": "4vh"
});

/* reveal.css */
$('.reveal .playback').css({
	"top": "75vh"
});