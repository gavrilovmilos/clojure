function login()
{
    document.Form1.action = "/login";
   // document.Form1.target = "_blank";    // Open in a new window
    document.Form1.submit();             // Submit the page
    return true;
}

function register()
{
    document.Form1.action = "/register";
   // document.Form1.target = "_blank";    // Open in a new window
    document.Form1.submit();             // Submit the page
    return true;
}