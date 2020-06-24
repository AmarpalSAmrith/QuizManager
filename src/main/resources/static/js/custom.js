// function myFunction(identifier, element) {
function myFunction(element) {
  var element = document.getElementById(element);
  element.parentNode.removeChild(element);
}