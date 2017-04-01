
	function createBokeh(numberOfBokehs) {
	   
	   // Variables to generate the bokehs
	   var bokehMinSize = 20;
	   var bokehMaxSize = 150;
	   var orbColour = '255, 255, 255'; //'10, 30, 70'; // Dark blue if no random colours are used
	   var useRandomColours = false;
	   var useGradients = true;
	   
	   // Generate the bokeh orbs
	   for(var i = 0; i < numberOfBokehs; i++) {
	      
	      // Generate a random bokeh size
	      var bokehSize = randomXToY(bokehMinSize, bokehMaxSize);
	      
	      if(useRandomColours) {
	         // Generate the random bokeh colour
	         var bokehColour = randomColour();
	      } else {
	         // Use the given RGB code
	         var bokehColour = orbColour;
	      }

	      var left = Math.floor(Math.random()* (screen.width-bokehSize) );
	      var top = Math.floor(Math.random()* (screen.height-bokehSize) );
	      
	      // Create the bokeh
	      var bokeh = $("<div />")
	         .addClass("bokeh")
	         .css({
	               // Use the max screen width and height to position the bokeh
	               'left' : left + 'px',
	               'top' : top + 'px',
	               'width' : bokehSize + 'px',
	               'height' : bokehSize + 'px',
	               '-moz-border-radius' : Math.floor(bokehSize)/2 + 'px',
	               '-webkit-border-radius' : Math.floor(bokehSize)/2 + 'px'//,
	               //'border' : '1px solid rgba(' + bokehColour + ', 0.7)'
	            });
	            
	      if(useGradients){
	         bokeh.css({
	            // Gradients for Firefox
	            'background' : '-moz-radial-gradient( contain, rgba('+ bokehColour +', 0.1), rgba(' + bokehColour + ',0.4))',
	            // Freaking ugly workaround to make gradients work for Safari too, by applying it to the background-image
	            'background-image' : '-webkit-gradient(radial, center center, 0, center center, 70.5, from(rgba('+ bokehColour +', 0.1)), to(rgba(' + bokehColour + ',0.4)))'
	         });
	      } else {
	         bokeh.css({
	            'background' : 'rgba(' + bokehColour + ', 0.3)'
	         });
	      }
	   
	      // Append to container
	      bokeh.appendTo("#bokehs");
	   }
	}
	
	//Function to get a random value between two values
	function randomXToY(minVal,maxVal,floatVal) {
	  var randVal = minVal+(Math.random()*(maxVal-minVal));
	  return typeof floatVal=='undefined'?Math.round(randVal):randVal.toFixed(floatVal);
	}
	 
	// Function to generate a random colour in RGB
	function randomColour() {
	  var rint = Math.round(0xffffff * Math.random());
	  return (rint >> 16) + ',' + (rint >> 8 & 255) + ',' + (rint & 255);
	}