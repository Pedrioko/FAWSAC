zk.plyr.Plyr = zk.$extends(zk.Widget, {

    _src: '',
    _muted: false,
    _autoplay: false,
    _controls: false,
    _loop: false,
    _preload: 'none',
    _poster: '',
    _crossorigin: '',
    _playbackRate: 1,
    _currentTime: '',
    getSrc: function () {
        return this._src;
    },
    setSrc: function (value) {
        if (this._src != value) {
            this._src = value;
        }
    },
    getCurrentTime: function () {
        return this._currentTime;
    },
    setCurrentTime: function (value) {
        if (this._currentTime != value) {
            this._currentTime = value;
        }
    },
    getPreload: function () {
        return this._preload;
    },
    setPreload: function (value) {
        if (this._preload != value) {
            this._preload = value;
        }
    },
    getPoster: function () {
        return this._poster;
    },
    getPlaybackRate: function (a) {
        return this._playbackRate;
    },
    setPlaybackRate: function (a) {
        try {
            var ctx = document.getElementById(this.uuid + '-video');
            if (ctx) {
                ctx.playbackRate = value;
            }
        } catch (b) {
            zk.error(b.message || b)
        }
    },
    setPoster: function (value) {
        if (this._poster != value) {
            this._poster = value;
        }
    },
    getCrossorigin: function () {
        return this._crossorigin;
    },
    setCrossorigin: function (value) {
        if (this._crossorigin != value) {
            this._crossorigin = value;
        }
    },
    getMuted: function () {
        return this._muted;
    },
    setMuted: function (value) {
        if (this._muted != value) {
            this._muted = value;
        }
    },
    getAutoplay: function () {
        return this._autoplay;
    },
    setAutoplay: function (value) {
        if (this._autoplay != value) {
            this._autoplay = value;
        }
    },
    getControls: function () {
        return this._controls;
    },
    setControls: function (value) {
        if (this._controls != value) {
            this._controls = value;
        }
    },
    getLoop: function () {
        return this._loop;
    },
    setLoop: function (value) {
        if (this._loop != value) {
            this._loop = value;
        }
    },
    setPlaying: function (value) {
        var ctx = document.getElementById(this.uuid + '-video');
        if (ctx) {
       //     ctx.currentTime = value;
        }
    },
    setVolume: function (a) {
        var ctx = document.getElementById(this.uuid + '-video');
        if (ctx) {
            ctx.volume = value;
        }
    },
    setMuted: function (a) {
        var ctx = document.getElementById(this.uuid + '-video');
        if (ctx) {
            ctx.muted = value;
        }
    },
    setCurrentTime: function (value) {
        var ctx = document.getElementById(this.uuid + '-video');
        if (ctx) {
            ctx.currentTime = parseFloat(value);
        }
    },
    bind_: function () {
        this.$supers('bind_', arguments);
        var elementId = this.uuid + '-video';
        var ctx = $('#'+elementId);
        const player = new Plyr('#' + elementId);

        player.source = {
            type: 'video',
            sources: [
                {
                    src: this.getSrc()
                }
            ]
          }
            player.on('playing', function (event) {
                var currentTime = event.detail.plyr.media.currentTime;
                var n = '#' + this.id + '-video';
                var widget = zk.Widget.$(elementId);
                widget.fire("onPlaying", { currentTime: currentTime }, { toServer: !0 }, 90)
                //zAu.send(new zk.Event(widget, 'setPlaying', {playing: 'true', currentTime: currentTime}, {toServer: true}));
            });
            player.on('pause', function (event) {
                var currentTime = event.detail.plyr.media.currentTime;
                var n = '#' + this.id + '-video';
                var widget = zk.Widget.$(elementId);
                widget.fire("onPause", { currentTime: currentTime }, { toServer: !0 }, 90)

                //zAu.send(new zk.Event(widget, 'setPlaying', {playing: 'false', currentTime: currentTime}, {toServer: true}));
            });
    },
    unbind_: function () {
        this.$supers('unbind_', arguments);
    },
    _videoDomAttrs: function () {
        var a = '';
        this._muted && (a += ' muted ');
        this._autoplay && (a += ' autoplay ');
        this._controls && (a += ' controls ');
        this._loop && (a += ' loop ');
        this._playsinline && (a += ' playsinline ');
        this._preload && (a += ' preload=\"' + this._preload + '\" ');
        this._poster && (a += ' poster=\"' + this._poster + '\" ');
        this._crossorigin && (a += ' crossorigin=\"' + this._crossorigin + '\" ');
        this._style && (a += ' style=\"' + this._style + '\" ');
        this._height && (a += ' height=\"' + this._height + '\" ');

        a += ' class=\" ' + this.getSclass() + ' \" '
        return a
    }
});