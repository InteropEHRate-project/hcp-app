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

subscriber = new EventSubscriber();

window.onload = function () {
    subscriber.start();
};

window.onbeforeunload = function () {
    subscriber.stop();
}