// 页面加载之后动态绑定事件
$(function(){
   $("#topBtn").click(setTop); 
   $("#wonderfulBtn").click(setWonderful); 
   $("#deleteBtn").click(setDelete); 
});

function like(btn, entityType, entityId, entityUserId, postId) {
    $.post(
        CONTEXT_PATH + "/like",
        {"entityType":entityType,"entityId":entityId,"entityUserId":entityUserId,"postId":postId},
        function(data) {
            data = $.parseJSON(data);
            if(data.code == 0) {
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?'已赞':"赞");
            } else {
                alert(data.msg);
            }
        }
    );
}

// 置顶 取消置顶
function setTop() {
    var postType = $('#postType').val();
    // 置顶
    if (postType != null && postType == 0) {
        $.post(
            CONTEXT_PATH + "/discuss/top",
            {"id": $("#postId").val()},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    //$("#topBtn").attr("disabled", "disabled");
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
    }
    // 取消置顶
    if (postType != null && postType == 1) {
        $.post(
            CONTEXT_PATH + "/discuss/untop",
            {"id": $("#postId").val()},
            function (data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
    }
}

// 加精 取消加精
function setWonderful() {
    var postStatus = $('#postStatus').val();
    
    // 加精
    if (postStatus != null && postStatus == 0) {
        $.post(
            CONTEXT_PATH + "/discuss/wonderful",
            {"id":$("#postId").val()},
            function(data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    // $("#wonderfulBtn").attr("disabled", "disabled");
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
    }
    // 取消加精
    if (postStatus != null && postStatus == 1) {
        $.post(
            CONTEXT_PATH + "/discuss/unwonderful",
            {"id":$("#postId").val()},
            function(data) {
                data = $.parseJSON(data);
                if (data.code == 0) {
                    // $("#wonderfulBtn").attr("disabled", "disabled");
                    window.location.reload();
                } else {
                    alert(data.msg);
                }
            }
        );
    }
}

// 删除
function setDelete() {
    $.post(
        CONTEXT_PATH + "/discuss/delete",
        {"id":$("#postId").val()},
        function(data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                location.href = CONTEXT_PATH + "/index";
            } else {
                alert(data.msg);
            }
        }
    );
}