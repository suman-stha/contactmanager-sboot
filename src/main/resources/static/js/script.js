console.log("This is script file.")

const toggleSidebar = () => {
    if ($('.sidebar').is(':visible')) {

        //true
        //need to close
        //none means hide the display
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
    }
    else {
        //false
        //need to show
        //block means show the display
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "20%");
    }
};

const search = () => {
    let query = $("#search-input").val();
    console.log(query);
    if (query == "") {
        $(".search-result").hide();

    }
    else {
        console.log(query);

        //sending request to server
        let url = `http://localhost:8383/search/${query}`;
        fetch(url).then((response) => {
            return response.json();
        })
            .then((data) => {
                //data...  
                console.log(data);

            });


        $(".search-result").show();
    }
};
