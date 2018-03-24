//make connection
var socket=io.connect('192.168.43.82:4000');

// Query DOM
var message=document.getElementById('message');
var handle=document.getElementById('handle');
var btn=document.getElementById('send');
var output=document.getElementById('output');
var feedback=document.getElementById('feedback');

//emitting event
btn.addEventListener('click',function(){
    socket.emit('chat',{
        message: message.value,
        handle: handle.value
    });
});

//emitting event for 'someone is typing'
message.addEventListener('keypress',function(){
    socket.emit('typing',handle.value);
});

//listen for events
socket.on('chat',function(data){
    feedback.innerHTML="";
    //manipulating the dom
    output.innerHTML+='<p><strong>'+data.handle+':</strong> '+ data.message+'</p>';
});

socket.on('typing',function(data){
    feedback.innerHTML='<p><em>'+data+' is typing a message...</em></p>';
});
