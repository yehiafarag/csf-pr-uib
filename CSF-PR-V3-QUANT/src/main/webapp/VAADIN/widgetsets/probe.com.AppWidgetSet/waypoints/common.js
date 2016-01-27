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

// If You have a custom component that contains a scrollable div,
// You might want to override this function
window.findScrollableOfContext = function(context, content) {
	var classList = context.classList;
	if (classList.contains("v-ui")) {
		// the v-ui element itself is the scrollable
		return context;
	} else if (classList.contains("v-panel")) {
		// v-panel v-scrollable
		// second child of panel is the scrollable
		return context.children[1];
	} else if (classList.contains("v-window")) {
		// v-window popupContent v-window-wrap v-window-contents
		// v-scrollable
		return context.children[0].children[0].children[2].children[0];
	} else if (classList.contains("v-tabsheet")) {
		// v-tabsheet v-tabsheet-content v-tabsheet-tabsheetpanel v-scrollable
		return context.children[1].children[0].children[0];
	} else if (classList.contains("v-accordion")) {
		// v-accordion v-accordion-item v-accordion-item-content
		// v-accordion has a scrollable for each of its tabs. try to find the
		// correct scrollable for element
		var items = $(context).children();
		var filtered = items.has(content);
		var item = filtered.first()[0];
		return item.children[1]; // second child of item is scrollable
	} else if (classList.contains("v-table")) {
		// v-table v-scrollable (this also includes treetable)
		return context.children[1];
	} else if (classList.contains("v-splitpanel-vertical")
			|| classList.contains("v-splitpanel-horizontal")) {
		// v-splitpanel-horizontal div v-scrollable
		// splitpanel has two different scrollables. try to find the correct
		// scrollable for element
		var containers = $(context.children[0]).children();
		var filtered = containers.has(content);
		var container = filtered.first()[0];
		return container;
	} else {
		console
				.warn("no handler for getting scrollable of context by classlist: "
						+ classList);
		// use the dom element itself if no handler was found
		return context;
	}
}
