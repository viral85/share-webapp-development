modules = {

    'smartpaper-css' {
        dependsOn 'jquery-ui'
        defaultBundle 'smartpaper-css'
        resource url:'css/reset.css'
        resource url:'css/typo.css'
        resource url:'css/jquery-ui.css'
        resource url:'css/uniform.default.css'
        resource url:'css/jquery.formbuilder.css'
        resource url:'css/button.css'
        resource url:'css/form.css'
        resource url:'css/skin.css'
        resource url:'css/main.css'
        resource url:'css/sandbox.css'
    }

    'smartpaper-jquery' {
        dependsOn 'jquery'
        defaultBundle 'smartpaper-jquery'
        resource url:'js/jquery.mousewheel.js', disposition: 'head'
        resource url:'js/jquery.uniform.min.js', disposition: 'head'
        resource url:'js/jquery.jscrollpane.min.js', disposition: 'head'
        resource url:'css/jquery.jscrollpane.css'
        resource url:'js/jquery.validate.js', disposition: 'head'
        resource url:'js/jquery.placeholder.min.js', disposition: 'head'
    }

    'cleditor' {
        resource url:'css/jquery.cleditor.css'
        resource url:'js/jquery.cleditor.min.js', disposition: 'head'
        resource url:'js/jquery.cleditor.xhtml.min.js', disposition: 'head'
        resource url:'js/jquery.cleditor.advancedtable.min.js', disposition: 'head'
    }

    'uploader' {
        resource url:'css/uploader.css'
        resource url:'js/fileuploader.js', disposition: 'head'
    }

    'farbtastic' {
        resource url:'css/farbtastic.css'
        resource url:'js/farbtastic.js', disposition: 'head'
    }

    'smartpaper-js' {
        dependsOn 'smartpaper-jquery', 'mediaplayer', 'cleditor'
        defaultBundle 'smartpaper-js'
        resource url:'js/main.js', disposition: 'head'
    }

    'mediaplayer'{
        resource url:'css/video-js.css'
        resource url:'js/video.js', disposition: 'head'
        resource url:'js/audio-player.js', disposition: 'head'
    }

    'preview-iphone' {
        dependsOn 'jquery','jquery-ui','mediaplayer', 'overscroll'
        resource url:'css/iphone.css'
    }

    'overscroll' {
        resource url:'js/jquery.overscroll.min.js', disposition: 'head'
    }
}