// Load the YouTube IFrame Player API
var tag = document.createElement('script');
tag.src = "https://www.youtube.com/iframe_api";
var firstScriptTag = document.getElementsByTagName('script')[0];
firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

// Declare the player variable
var player;

// Function called when the API is ready
function onYouTubeIframeAPIReady() {
    player = new YT.Player('player', {
        height: '360', // Set the height of the player
        width: '640',  // Set the width of the player
        videoId: 'GLNni7IL268', // YouTube video ID
        playerVars: {
            autoplay: 1, // Autoplay the video
            controls: 1, // Show player controls
            rel: 0,      // Don't show related videos at the end
          },
          events: {
            'onReady': onPlayerReady,
            'onError': onPlayerError
          }
    });
}

// Called when the player is ready
function onPlayerReady(event) {
    console.log("Player is ready!");
    // Uncomment to autoplay the video when the player is ready:
    // event.target.playVideo();
}

// Handle errors (e.g., embedding restrictions)
function onPlayerError(event) {
    console.error('An error occurred:', event.data);
  }
