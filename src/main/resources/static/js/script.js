console.log("This is script file.")

const toggleSidebar = () => {
    if ($('.sidebar').is(':visible')) {

        //true
        //need to close
        //none means hide the display
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%")
    }
    else {
        //false
        //need to show
        //block means show the display
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%")
    }
};