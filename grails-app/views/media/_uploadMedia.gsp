<div class="upload-media">
    <div class="fileupload">
        <uploader:uploader id="image" url="[controller:'media',action:'upload', params:[screen:params.screen?:null,application:request.application.id,trigger:trigger?:false]]" multiple="false" allowedExtensions="${allowedExtensions?:[]}">
            <uploader:onProgress>
                $( "#progressbar" ).progressbar({value: 100*loaded/total});
                $( "#progressbar-val" ).html(Math.round(100*loaded/total)+'%');
            </uploader:onProgress>
            <uploader:onComplete>
                if (!responseJSON.media){
                    alert("${message(code:'ui.application.error.upload')}");
                }else{
                    $.smartpaper.manageMediaUploaded(responseJSON.media,responseJSON.content);
                }
            </uploader:onComplete>
        </uploader:uploader>
        <div id="progressbar"></div><div id="progressbar-val"></div>
    </div>
    <div class="edit-image" id='edit-media'>

    </div>
</div>