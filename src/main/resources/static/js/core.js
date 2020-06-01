function simple_scan() {
    var ip = $("#ip").val();
    var port = $("#port").val();

    if(ip==null||ip.trim()===""){
        mdui.alert('请输入域名或IP地址！','系统提示');
        return;
    }

    if(port==null||port.trim()===""){
        mdui.alert('请输入端口号！', '系统提示');
        return;
    }

    $("#result_ip").text(ip);
    $("#result_port").text(port);
    $("#result_status").text("扫描中...");
    $("#result_protocol").text("扫描中...");
    $("#result_service").text("扫描中...");

    var settings = {
        "url": "http://localhost:8080/port-scanner/api/v1/scan/simple",
        "method": "POST",
        "timeout": 0,
        "data": "{\n\t\"ip\":\"" + ip + "\",\n\t\"port\":\"" + port + "\"\n}",
        "headers": {
            "Content-Type": "application/json"
        }
    };

    $.ajax(settings).done(function (response) {
        console.log(response.code)
        if (response.code !== '1') {
            mdui.alert(response.message, '系统提示');
            $("#result_ip").text('');
            $("#result_port").text('');
            $("#result_status").text('');
            $("#result_protocol").text('');
            $("#result_service").text('');
            return;
        }
        var data = response.data;
        $("#result_ip").text(data.ip);
        $("#result_port").text(data.port);
        $("#result_status").text(data.status);
        $("#result_protocol").text(data.protocol);
        $("#result_service").text(data.service);
    });
}