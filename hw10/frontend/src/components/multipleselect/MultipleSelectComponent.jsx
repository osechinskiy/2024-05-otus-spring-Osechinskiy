import React, {useEffect, useState} from 'react';
import {FormControl, FormHelperText, InputLabel, MenuItem, Select} from "@mui/material";
import OutlinedInput from '@mui/material/OutlinedInput';
import {useTheme} from '@mui/material/styles';

const ITEM_HEIGHT = 48;
const ITEM_PADDING_TOP = 8;
const MenuProps = {
    PaperProps: {
        style: {
            maxHeight: ITEM_HEIGHT * 4.5 + ITEM_PADDING_TOP,
            width: 250,
        },
    },
};

function getStyles(item, personName, theme) {
    return {
        fontWeight:
            personName.indexOf(item) === -1
                ? theme.typography.fontWeightRegular
                : theme.typography.fontWeightMedium,
    };
}

const MultipleSelectComponent = (props) => {

    const theme = useTheme();
    const [genre, setGenre] = useState([]);
    const {sx, data, action, errors, genresInfo} = props

    const handleChange = (event) => {
        const {target: {value}} = event;
        setGenre(
            typeof value === 'string' ? value.split(',') : value,
        )
    };

    useEffect(() => {
        if (genresInfo && genre.length === 0) {
            let genresId = genresInfo.map(row => row.id)
            setGenre(typeof genresId === 'string' ? genresId.split(',') : genresId)
        }
        action(genre)
    }, [action, genre, genresInfo])

    const map = errors.badFields;

    return (
        <React.Fragment>
            <FormControl
                sx={sx}
                error={errors.hasError === true && 'genres' in map}>
                <InputLabel id="demo-multiple-name-label">Жанры</InputLabel>
                <Select
                    labelId="demo-multiple-name-label"
                    id="demo-multiple-name"
                    multiple
                    value={genre}
                    onChange={handleChange}
                    input={<OutlinedInput label="Жанры"/>}
                    MenuProps={MenuProps}>
                    {data && data.map((item) => (
                        <MenuItem
                            key={item.id}
                            value={item.id}
                            style={getStyles(item.name, genre, theme)}>
                            {item.name}
                        </MenuItem>
                    ))}
                </Select>
                {errors.hasError === true && 'genres' in map ? (
                    <FormHelperText>{map.genres}</FormHelperText>
                ) : null}
            </FormControl>
        </React.Fragment>
    )
}

export default MultipleSelectComponent;