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


/* custom.css */
$('#graph-panel').css({
	"height": "38vh"
});

$('#student-level').css({
	"padding-top": "7vh",
	"padding-right": "6vh"
});

$('.custom-inspinia-timeline').css({
	"min-height": "10vh"
});

$('.question-footer .custom-inspinia-timeline, .question-footer .custom-timeline-item').css({
	"height": "8vh"
});

$('.question-footer').css({
	"height": "10vh"
});


/* gcard.css */
$('hr.vertical-line').css({
	"height": "12vh"
});

$('.gcard.percentiles').css({
	"min-height": "12vh"
});

$('.item-3').css({
	"height": "12vh"
});

$('.item-4').css({
	"height": "12vh"
});

$('.item-2').css({
	"height": "6vh"
});

$('.gpanel-tab-header .item-2').css({
	"height": "8vh"
});

$('.gpanel-header').css({
	"min-height": "8vh"
});

$('.gpanel-tab-header').css({
	"min-height": "8vh"
});

$('i.toggle-visibility').css({
	"line-height": "4.5vh"
});

$('.graph_container').css({
	"height": "41vh",
	"padding-bottom": "1vh"
});

$('.qustion-text').css({
	"height": "31vh"
});

$('.explanation-text').css({
	"height": "31vh"
});

$('.gcard.single').css({
	"min-height": "12vh",
	"height": "22vh"
});


/* card-flip.css */
$('.flip-card-container').css({
	"min-height": "52vh"
});


//report.jsp
$('#wrapper').css({
	"min-height": "100vh"
});

$('#mimic-header-gap').css({
	"margin-top": "2vh"
});

$('#create-graph-parent-div').css({
	"min-height": "42vh"
});

$('.item-2').css({
	"height": "6vh"
});


/* learning_analysis.jsp */
$('.learning-analysis-child-skill').css({
	"min-height": "55vh"
});


/* no_report_available.jsp */
$('.no-report-img-div').css({
	"height": "200vh"
});


/* test_skill_list.jsp */
$('.test-skill-list-test-child-div').css({
	"height": "55vh"
});

//