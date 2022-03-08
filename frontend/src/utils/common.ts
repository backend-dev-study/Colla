interface Object {
    [key: string]: string | File | null;
}

export const createFormData = (data: Object) => {
    const formData = new FormData();
    Object.keys(data).forEach((key) => formData.append(key, data[key]!));
    return formData;
};

export const YYYYMMDDToDate = (str: string) => {
    const date = new Date(
        parseInt(str.slice(0, 4), 10),
        parseInt(str.slice(4, 6), 10) - 1,
        parseInt(str.slice(6, 8), 10),
    );
    return date;
};
