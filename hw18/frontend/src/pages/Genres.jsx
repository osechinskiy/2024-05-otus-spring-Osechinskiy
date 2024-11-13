import React from 'react';
import TitleContainer from "../components/container/TitleContainer.jsx";
import GenresTable from "../view/genres/GenresTable.jsx";

const Genres = () => {
    return (
        <React.Fragment>
            <TitleContainer text="Информация о жанрах"/>
            <GenresTable/>
        </React.Fragment>
    )
}

export default Genres