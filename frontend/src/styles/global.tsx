import React from 'react';
import { css, Global } from '@emotion/react';

const GlobalStyle = () => (
    <Global
        styles={css`
            button {
                border: none;
                &:hover {
                    opacity: 50%;
                    cursor: pointer;
                }
            }

            input {
                border: none;
                &:focus {
                    outline: none;
                }
            }

            @font-face {
                font-family: 'Euljiro';
                src: url('/assets/fonts/Euljiro.ttf');
                font-style: normal;
                font-weight: 400;
                font-display: swap;
            }

            * {
                font-family: 'Euljiro';
            }
        `}
    />
);

export default GlobalStyle;
