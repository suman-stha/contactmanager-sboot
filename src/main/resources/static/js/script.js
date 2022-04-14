console.log("This is script file.")

const toggleSidebar = () => {
    if ($('.sidebar').is(':visible')) {

        //true
        //need to close
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%")
    }
    else {
        //false
        //need to show
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%")
    }
};