function EventSubscriber() {
    this.source = null;
    this.start = function () {
        this.source = new EventSource("/hcp-web-ui/d2d/connection/events/stream");

        this.source.addEventListener("message", function (event) {
            const message = JSON.parse(event.data);
            if (message.action === "RELOAD_PAGE") {
                document.location.reload();
            }
        });
    };

    this.stop = function () {
        this.source.close();
    }
}

function CloudEventSubscriber() {
    this.cloudSource = null;
    this.start = function () {
        this.cloudSource = new EventSource("/hcp-web-ui/cloud/connection/events/stream");

        this.cloudSource.addEventListener("message", function (event) {
            const message = JSON.parse(event.data);
            if (message.action === "RELOAD_PAGE") {
                document.location.reload();
            }
        });
    };

    this.stop = function () {
        this.cloudSource.close();
    }
}

subscriber = new EventSubscriber();
cloudSubscriber = new CloudEventSubscriber();

window.onload = function () {
    subscriber.start();
    cloudSubscriber.start();
};

window.onbeforeunload = function () {
    subscriber.stop();
    cloudSubscriber.stop();
}