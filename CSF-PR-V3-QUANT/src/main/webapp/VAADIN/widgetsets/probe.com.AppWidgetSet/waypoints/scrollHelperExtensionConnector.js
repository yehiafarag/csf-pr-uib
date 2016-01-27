/*
 * The MIT License (MIT)
 * 
 * Copyright (c) 2015 Christian Duße
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * 
 */

window.eu_dusse_vaadin_waypoints_ScrollHelper = function() {

	// extension connector
	var connector = this;

	this.scrollTo = function(target, context, horizontal, offset, speed) {
		var targetElement = connector.getElement(target);
		
		var contextElement; // the scrollable element
		if (context == null) {
			// when no context was specified -> use window
			contextElement = window;
		} else {
			// otherwise use specified context
			contextElement = connector.getElement(context);

			if (context == null) {
				throw "No element found for specified context: "
						+ context
			}

			// find scrollable dom for vaadin component dom
			contextElement = window.findScrollableOfContext(contextElement, targetElement);
		}
		
		// call jquery.smooth-scroll plugin
		$.smoothScroll({
			scrollElement: $(contextElement),
		    scrollTarget: $(targetElement),
		    offset: offset,
		    direction: (horizontal ? 'left' : 'top'),
		    speed: speed
		});
	}

}