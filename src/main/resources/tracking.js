let visitId;

// When the page loads, start tracking
window.onload = function () {
    const pageUrl = window.location.href;

    fetch('/page-visit/start?pageUrl=' + encodeURIComponent(pageUrl), {
        method: 'POST'
    })
        .then(response => response.json())
        .then(data => {
            console.log('Visit ID:', data.id); // Debugging log
            visitId = data.id;
        })
        .catch(error => console.error('Error starting page visit:', error));
};

// When the user navigates away, end tracking
window.onbeforeunload = function () {
    if (visitId) {
        // Use navigator.sendBeacon for reliability
        const url = '/page-visit/end/' + visitId;
        navigator.sendBeacon(url);
    }
};
