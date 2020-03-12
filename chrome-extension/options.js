// Saves options to chrome.storage
function save_options() {
  var endpoint = document.getElementById("endpoint").value;
  console.log(endpoint);
  chrome.storage.local.set(
    {
      endpoint: endpoint
    },
    function() {
      // Update status to let user know options were saved.
      var status = document.getElementById("status");
      status.textContent = "Options saved.";
      setTimeout(function() {
        status.textContent = "";
      }, 1500);
    }
  );
  chrome.runtime.reload();
}

// Restores input form state using the preferences stored in chrome.storage.
function restore_options() {
  // Use default value color = 'default'
  chrome.storage.local.get(
    {
      endpoint: "default-endpoint"
    },
    function(items) {
      document.getElementById("endpoint").value = items.endpoint;
    }
  );
}
document.addEventListener("DOMContentLoaded", restore_options);
document.getElementById("save").addEventListener("click", save_options);
