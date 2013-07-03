<div class="upload-media">
    <div class="fileupload">
        <uploader:uploader id="image" url="[controller:'app',action:'uploadTrigger', params:[application:request.application.id]]" multiple="false" allowedExtensions="${allowedExtensions?:[]}">
            <uploader:onProgress>
                $( "#progressbar" ).progressbar({value: 100*loaded/total});
                $( "#progressbar-val" ).html(Math.round(100*loaded/total)+'%');
            </uploader:onProgress>
            <uploader:onComplete>
                $.smartpaper.manageMediaUploaded(responseJSON.media);
            </uploader:onComplete>
        </uploader:uploader>
        <div id="progressbar"></div><div id="progressbar-val"></div>
    </div>
    <div class="edit-image" id='edit-media'>

    </div>
</div>