import React, {useEffect, useState} from 'react';
import {TextField} from "@mui/material";

const TextInputComponent = (props) => {

    const {text, action, sx, errors, data} = props
    const [title, setTitle] = useState('')

    const titleHandleChange = (event) => {
        setTitle(event.target.value);
    };
    const map = errors.badFields;
    useEffect(() => {
        if (data && data.title && title === '') {
            setTitle(data.title);
        }
        action(title)
    }, [action, data, title])
    return (
        <React.Fragment>
            <TextField
                error={errors.hasError === true && 'title' in map}
                helperText={errors.hasError === true && 'title' in map ? map.title : null}
                onChange={titleHandleChange}
                sx={sx} id="outlined-basic"
                label={text}
                value={title}
                variant="outlined"/>
        </React.Fragment>
    )
}

export default TextInputComponent