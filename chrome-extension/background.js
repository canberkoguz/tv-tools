// --------------------------
// Open Event Stream
// --------------------------

function start(endpoint) {
  if (!window.EventSource) {
    log("The browser doesn't support EventSource.");
    return;
  }

  eventSource = new EventSource(endpoint);

  eventSource.onopen = function(e) {
    log("Event: open");
  };

  eventSource.onerror = function(e) {
    if (this.readyState == EventSource.CONNECTING) {
      log(`Reconnecting (readyState=${this.readyState})...`);
    } else {
      log("Error has occured.");
    }
  };

  eventSource.onmessage = function(e) {
    log(e.data);
    display(e.data);
  };
}

function stop() {
  eventSource.close();
  log("eventSource.close()");
}

// ---------------------
// Opening messages
// ---------------------

function openURL(url) {
  let urlWindow = window.open(url, "_blank");
  setTimeout(() => urlWindow.close(), 5000);
}

function openImage(url) {
  const getHTML = string => {
    return `<body style="background-image:url('${string}');background-repeat: no-repeat; background-size: contain; background-position: center; background-color: black"></body>`;
  };

  let imgWindow = window.open("about:blank", "_blank");
  imgWindow.document.write(getHTML(url));
  setTimeout(() => imgWindow.close(), 5000);
}

function openText(string) {
  const getHTML = text => {
    return `<body style="display: flex; height: 100vh; margin: 0; align-items: center; justify-content: center;" ><h1 style="font-size: 15rem;">${text}</h1><body>`;
  };

  let msgWindow = window.open("about:blank", "_blank");
  msgWindow.document.write(getHTML(string));
  setTimeout(() => msgWindow.close(), 5000);
}

// ---------------------
// Display
// ---------------------

const urlRegex = /^(ftp|http|https|file):\/\/[^ "]+$/i;
const imgRegex = /\.(?:jpe?g|gif|png)$/i;

function display(msg) {
  let trimmedMsg = msg.trim();
  if (urlRegex.test(trimmedMsg)) {
    imgRegex.test(trimmedMsg) ? openImage(trimmedMsg) : openURL(trimmedMsg);
  } else {
    openText(msg);
  }
}

function log(msg) {
  console.log(msg);
}

//-----------------------
// Get endpoint and run!
//-----------------------

chrome.storage.local.get(["endpoint"], function(result) {
  log("Current Endpoint: " + result.endpoint);
  start(result.endpoint);
});

log("Background script started");
