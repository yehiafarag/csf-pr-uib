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

window.eu_dusse_vaadin_waypoints_WaypointExtensionImpl = function() {

	// extension connector
	var connector = this;

	// id of extended connector
	var parentConnectorId = connector.getParentId();

	// element of extended connector
	var element = connector.getElement(parentConnectorId);

	// connector id of context (scrollable)
	var contextConnectorId = connector.getState().contextConnectorId;

	var context; // the scrollable element
	if (contextConnectorId == null) {
		// when no context was specified -> use window
		context = window;
	} else {
		// otherwise use specified context
		context = connector.getElement(contextConnectorId);

		if (context == null) {
			throw "No element found for specified contextConnectorId: "
					+ contextConnectorId;
		}

		// find scrollable dom for vaadin component dom
		context = window.findScrollableOfContext(context, element);
	}

	var waypoint = new Waypoint({
		element : element,
		handler : function(direction) {
			if (connector.getState().hasListeners) {
				connector.crossed(direction);
			}
		},
		context : context,
		horizontal : connector.getState().horizontal,
		offset : connector.getState().offset
	});

	this.onStateChange = function() {
		if (connector.getState().enabled) {
			waypoint.enable();
		} else {
			waypoint.disable();
		}
	}

	this.destroy = function() {
		waypoint.destroy();
	}

}