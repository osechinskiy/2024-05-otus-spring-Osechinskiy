import React, {useEffect, useState} from 'react';
import {FormControl, FormHelperText, InputLabel, MenuItem, Select} from "@mui/material";

const SelectComponent = (props) => {

    const {data, action, sx, errors, authorInfo} = props;
    const [author, setAuthor] = useState('');

    const authorHandleChange = (event) => {
        setAuthor(event.target.value);
    };
    const map = errors.badFields;

    useEffect(() => {
        if (authorInfo && authorInfo.fullName && author === '') {
            setAuthor(authorInfo.id);
        }
        action(author)
    }, [action, author, authorInfo])

    return (
        <React.Fragment>
            <FormControl
                sx={sx}
                error={errors.hasError === true && 'authorId' in map}>
                <InputLabel id="author-label">Автор</InputLabel>
                <Select
                    labelId="author-label"
                    id="author-select"
                    value={author}
                    label="Автор"
                    onChange={authorHandleChange}>
                    {data && data.map((item) => (
                        <MenuItem key={item.id} value={item.id}>{item.fullName}</MenuItem>
                    ))};
                </Select>
                {errors.hasError === true && 'authorId' in map ? (
                    <FormHelperText>{map.authorId}</FormHelperText>
                ) : null}
            </FormControl>
        </React.Fragment>
    )
}

export default SelectComponent;