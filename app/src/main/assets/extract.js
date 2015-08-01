function isScriptNode(node) {
    return node.nodeType === Node.ELEMENT_NODE && node.nodeName === "SCRIPT";
}

function isWhiteSpaceOnly(node) {
    return node.nodeValue.replace(/[\n\t ]+/, "") === "";
}

function getAllTextNodes(elem) {
    var filter = NodeFilter.SHOW_TEXT,
        walker = document.createTreeWalker(elem, filter, null, false),
        arr = [],
        node;
    while (walker.nextNode()) {
        node = walker.currentNode;
        if (node.parentNode && isScriptNode(node.parentNode)) {
            continue;
        }
        if (node.isElementContentWhitespace || isWhiteSpaceOnly(node)) {
            continue;
        }
        arr.push(node);
    }
    return arr;
}

function nodesToIndexedArray(nodes) {
    return nodes.map(function(item, index) {
        return {
            index: index,
            text: item.nodeValue
        };
    });
}

var data = nodesToIndexedArray(getAllTextNodes(document.body));
Android.passData(JSON.stringify(data));
