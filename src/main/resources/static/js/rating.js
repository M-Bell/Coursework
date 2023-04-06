const stars = document.querySelectorAll('.star');

stars.forEach(star => {
    star.addEventListener('click', () => {
        const recipeId = document.getElementById("rating").getAttribute("data-recipe-id");// get the recipe ID here
        const rating = star.dataset.value; // get the rating value from the data-value attribute
        window.location.href = `/rate-recipe/${recipeId}/${rating}`;
    });
});